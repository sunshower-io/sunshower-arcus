package io.sunshower.lang.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbstractEventSourceTest {

  private EventSource source;
  @Mock
  private EventListener eventListener;

  @BeforeEach
  void setUp() {
    source = new AbstractEventSource();

  }


  @Test
  void ensureEventDispatcherCanContainMultipleTypesOfEvents() {
    source.addEventListener(eventListener, () -> 0);
    source.addEventListener(eventListener, () -> 2);
    assertEquals(source.getListenerCount(), 2);
    for (val id : List.of(0, 2)) {
      assertTrue(source.listensFor(() -> id));
    }
  }

  @Test
  void ensureDispatchingEventWorks() {
    EventType type =  () -> 0;
    val event = Events.create("hello");
    source.addEventListener(eventListener, type);
    source.dispatchEvent(type, event);
    verify(eventListener).onEvent(type, event);

  }

}