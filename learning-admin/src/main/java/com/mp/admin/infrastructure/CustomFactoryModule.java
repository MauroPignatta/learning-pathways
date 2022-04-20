package com.mp.admin.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomFactoryModule extends SimpleModule {

    private final List<FactoryForType<?>> factories = new ArrayList<>();

    private static class CommandInstantiator extends ValueInstantiator.Base {
        private FactoryForType<?> factory;

        public CommandInstantiator(FactoryForType<?> theFactory) {
            super(theFactory.getTypeToReplace());
            this.factory = theFactory;
        }

        @Override
        public boolean canCreateUsingDefault() {
            return true;
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) {
            return factory.create();
        }

        @Override
        public Class<?> getValueClass() {
            return factory.getTypeToReplace();
        }
    }

    @Override
    public void setupModule(SetupContext context) {
        ObjectMapper mapper = context.getOwner();

        for (FactoryForType<?> factory : factories) {
            CommandInstantiator instantiator = new CommandInstantiator(factory);
            addValueInstantiator(factory.getTypeToReplace(), instantiator);

            String name = findNameFor(mapper, factory.getTypeToReplace());
            if(name != null) {
                registerSubtypes(new NamedType(factory.getType(), name));
                addValueInstantiator(factory.getType(), instantiator);
            }
        }
        super.setupModule(context);
    }

    private String findNameFor(ObjectMapper mapper, Class<?> type) {
        DeserializationConfig config = mapper.getDeserializationConfig();
        BeanDescription bean = config.introspectClassAnnotations(type);
        SubtypeResolver resolver = mapper.getSubtypeResolver();

        Collection<NamedType> subtypes =
            resolver.collectAndResolveSubtypesByTypeId(config, bean.getClassInfo());

        for (NamedType namedType : subtypes) {
            if (namedType.getType().equals(type)) {
                return namedType.getName();
            }
        }
        return null;
    }

    public void registerFactory(FactoryForType<?> factory) {
        factories.add(factory);
    }
}
