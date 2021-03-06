package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.transition.IFSMTransition;
import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class NextFSMTransitionBuilder<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<NextArgument>, NextArgument> {
	protected final IFSMType<CurrentState, CurrentArgument> current;

	protected final IFSMType<Event, EventArgument> event;

	protected final IPredicate2<? super CurrentArgument, ? super EventArgument> guard;

	protected final IFSMType<NextState, NextArgument> next;

	protected IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> argument;

	public NextFSMTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> argument(final IConsumer2<? super CurrentArgument, ? super EventArgument> argument) {
		setArgument(argument.toFunction(null));
		return this;
	}

	public NextFSMTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> argument(IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> argument) {
		setArgument(argument);
		return this;
	}

	public <Emission> IFSMTransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument, Emission> build() {
		return this.<Emission>emission(null).build();
	}

	public <Emission> EmitFSMTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument, Emission> emission(IFunction2<? super CurrentArgument, ? super EventArgument, ? extends Emission> emission) {
		return new EmitFSMTransitionBuilder<>(getCurrent(), getEvent(), getGuard(), getNext(), getArgument(), emission);
	}
}