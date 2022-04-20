package com.mp.admin.application;

import com.mp.admin.domain.TestRepository;
import com.mp.admin.infrastructure.FactoryForType;
import org.apache.commons.lang3.Validate;

/** A factory to build TestCommands. */
public class TestCommandFactory implements FactoryForType<TestCommand> {

  /** The testRepository needed to build TestCommands. Never null. */
  private TestRepository testRepository;

  /**
   * Constructor.
   *
   * @param theTestRepository The TestRepository needed by the TestCommands. Cannot be null.
   */
  public TestCommandFactory(TestRepository theTestRepository) {
    Validate.notNull(theTestRepository, "The test repository cannot be null");

    testRepository = theTestRepository;
  }

  /** {@inheritDoc} */
  @Override
  public Class<TestCommand> getType() {
    return TestCommand.class;
  }

  /** {@inheritDoc} */
  @Override
  public TestCommand create() {
    return new TestCommand(testRepository);
  }
}
