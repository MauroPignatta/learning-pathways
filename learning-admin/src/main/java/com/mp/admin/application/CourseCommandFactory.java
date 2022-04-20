package com.mp.admin.application;

import com.mp.admin.domain.CourseRepository;
import com.mp.admin.infrastructure.FactoryForType;

public class CourseCommandFactory implements FactoryForType<CourseCommand> {

    private CourseRepository courseRepository;

    public CourseCommandFactory(CourseRepository theCourseRepository) {
        courseRepository = theCourseRepository;
    }

    @Override
    public Class<CourseCommand> getType() {
        return CourseCommand.class;
    }

    @Override
    public CourseCommand create() {
        return new CourseCommand(courseRepository);
    }
}
