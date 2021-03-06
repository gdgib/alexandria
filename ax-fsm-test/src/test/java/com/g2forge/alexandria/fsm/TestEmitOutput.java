package com.g2forge.alexandria.fsm;

import static com.g2forge.alexandria.fsm.HFSM.transition;
import static com.g2forge.alexandria.fsm.HFSM.value;

import org.junit.Test;

public class TestEmitOutput {
	public static enum Event implements IFSMEnum<Event> {
		Up,
		Down;
	}

	public static enum State implements IFSMEnum<State> {
		Zero,
		One,
		Two;
	}

	protected static final FSMBuilder<Event, State, String, Integer> builder;

	static {
		builder = new FSMBuilder<Event, State, String, Integer>();
		builder.output(current -> ((State) current.getType()).ordinal());
		builder.transition(transition(State.Zero, Event.Up, State.One, "0+"));
		builder.transition(transition(State.One, Event.Up, State.Two, "1+"));
		builder.transition(transition(State.Two, Event.Down, State.One));
		builder.transition(transition(State.One, Event.Down, State.Zero));
	}

	@Test
	public void base() {
		final FSMTester<Event, State, String, Integer> tester = new FSMTester<>(builder, State.Zero);
		tester.assertEmission("0+", Event.Up).assertState(State.One).assertOutput(1);
		tester.assertEmission("1+", value(Event.Up)).assertState(State.Two).assertOutput(2);
		tester.assertEmission(null, Event.Down).assertStateType(State.One).assertOutput(1);
		tester.assertEmission(null, Event.Down).assertStateType(State.Zero).assertOutput(0);
	}

	@Test(expected = FSMDisallowedEventException.class)
	public void disallowTwoUp() {
		new FSMTester<>(builder, State.Two).fire(Event.Up);
	}

	@Test(expected = FSMDisallowedEventException.class)
	public void disallowZeroDown() {
		new FSMTester<>(builder, State.Zero).fire(Event.Down);
	}
}
