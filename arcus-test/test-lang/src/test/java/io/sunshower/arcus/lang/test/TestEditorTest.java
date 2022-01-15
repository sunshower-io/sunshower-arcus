package io.sunshower.arcus.lang.test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class TestEditorTest {

  @Test
  void ensureAppendWorks() {
    String[][] queries =
        new String[][] {
          new String[] {"APPEND", "Hey"},
          new String[] {"APPEND", " there"},
          new String[] {"APPEND", "!"}
        };

    String[] result = textEditor2_2(queries);
    System.out.println(Arrays.toString(result));
  }

  @Test
  void ensureMoveWorks() {
    String[][] queries =
        new String[][] {
          new String[] {"APPEND", "Hey you"},
          new String[] {"MOVE", "3"},
          new String[] {"APPEND", ","}
        };

    String[] result = textEditor2_2(queries);
    System.out.println(Arrays.toString(result));
  }

  @Test
  void ensureDeleteWorks() {
    String[][] queries =
        new String[][] {
          new String[] {"APPEND", "Hello! world!"},
          new String[] {"MOVE", "5"},
          new String[] {"DELETE"},
          new String[] {"APPEND", ","}
        };
    String[] result = textEditor2_2(queries);
    System.out.println(Arrays.toString(result));
  }

  @Test
  void ensureFirstTestWorks() {

    String[][] queries =
        new String[][] {
          new String[] {"APPEND", "Hey"},
          new String[] {"APPEND", " you"},
          new String[] {"APPEND", ", don't"},
          new String[] {"APPEND", " "},
          new String[] {"APPEND", "let me down"},
        };
    printOutput(textEditor2_2(queries));
    //    System.out.println((textEditor2_2(queries)));
  }

  private void printOutput(String[] results) {
    System.out.println("[");
    for (String q : results) {
      System.out.println(q);
    }
    System.out.println("]");
  }

  @Test
  void ensureSecondTestWorks() {

    String[][] queries =
        new String[][] {
          new String[] {"APPEND", "Hey, you!"},
          new String[] {"MOVE", "5"},
          new String[] {"DELETE"},
          new String[] {"DELETE"},
          new String[] {"DELETE"},
          new String[] {"DELETE"},
          new String[] {"DELETE"},
          new String[] {"APPEND", "little world!"}
        };
    printOutput(textEditor2_2(queries));
    //    System.out.println(Arrays.toString(textEditor2_2(queries)));

  }

  @Test
  void ensureThirdTestWorks() {

    String[][] queries =
        new String[][] {
          new String[] {"APPEND", "Never give up"},
          new String[] {"MOVE", "-10"},
          new String[] {"APPEND", "START."},
          new String[] {"MOVE", "20"},
          new String[] {"APPEND", "END."},
          new String[] {"DELETE"}
        };
    printOutput(textEditor2_2(queries));
  }

  enum OperationType {
    Append,
    Delete,
    Cut,
    Move,
    Paste,
    Undo,
    Redo,
    Select;

    static final Map<String, OperationType> opCache;

    static {
      opCache = new HashMap<>();
      for (OperationType opType : OperationType.values()) {
        opCache.put(opType.name().toLowerCase(Locale.ROOT), opType);
      }
    }

    public static OperationType parse(String value) {
      if (value == null) {
        throw new IllegalArgumentException("No operation named 'null' ");
      }
      String normalized = value.trim().toLowerCase(Locale.ROOT);
      if (normalized.isEmpty()) {
        throw new IllegalArgumentException(
            String.format("No operation type with value: %s", value));
      }
      OperationType result = opCache.get(normalized);
      if (result == null) {
        throw new NoSuchElementException(
            String.format("No element associated with name '%s'", value));
      }
      return result;
    }
  }

  interface Operation {

    OperationType getOperationType();

    int apply(Editor editor, StringBuilder textBuffer, int cursorPosition);

    String getContents();

    default boolean includeInHistory() {
      return true;
    }
  }

  static final class Editor {

    private final StringBuilder textBuffer;
    private final Deque<Operation> histories;
    private int cursorPosition;
    private int selectionEnd;
    private int selectionStart;

    Editor() {
      cursorPosition = 0;
      histories = new ArrayDeque<>();
      textBuffer = new StringBuilder();
    }

    public void apply(Operation operation) {
      histories.push(operation);
      cursorPosition = operation.apply(this, textBuffer, cursorPosition);
    }

    public void setCursorPosition(int position) {
      this.cursorPosition = position;
    }

    public String[] getHistoryAsString() {
      final Iterator<Operation> operationIterator = histories.descendingIterator();
      final Editor replayCopy = new Editor();

      List<String> operations = new ArrayList<>(histories.size());
      while (operationIterator.hasNext()) {
        Operation next = operationIterator.next();

        replayCopy.apply(next);
        if (next.includeInHistory()) {
          operations.add(new String(replayCopy.textBuffer));
        }
      }
      return operations.toArray(new String[0]);
    }

    public void select(int start, int end) {
      this.selectionEnd = end;
      this.selectionStart = start;
    }

    public boolean hasSelection() {
      return selectionEnd >= 0 && selectionStart >= 0;
    }

    public void clearSelection() {
      selectionEnd = -1;
      selectionStart = -1;
    }
  }

  abstract static class AbstractOperation implements Operation {

    final OperationType operationType;

    AbstractOperation(final OperationType operationType) {
      this.operationType = operationType;
    }

    public OperationType getOperationType() {
      return operationType;
    }
  }

  static final class SelectOperation extends AbstractOperation {

    private final int end;
    private final int start;

    SelectOperation(String start, String end) {
      super(OperationType.Select);
      this.end = Integer.parseInt(end);
      this.start = Integer.parseInt(start);
    }

    @Override
    public int apply(Editor editor, StringBuilder textBuffer, int cursorPosition) {
      editor.select(start, end);
      return cursorPosition;
    }

    @Override
    public String getContents() {
      return null;
    }
  }

  static final class DeleteOperation extends AbstractOperation {

    DeleteOperation() {
      super(OperationType.Delete);
    }

    @Override
    public int apply(Editor editor, StringBuilder textBuffer, int cursorPosition) {
      if (cursorPosition >= 0 && cursorPosition < textBuffer.length()) {
        textBuffer.delete(cursorPosition, cursorPosition + 1);
        return cursorPosition;
      }
      return cursorPosition;

      //      textBuffer.delete(cursorPosition - 1, cursorPosition);
      //      return cursorPosition - 1;
    }

    @Override
    public boolean includeInHistory() {
      return true;
    }

    @Override
    public String getContents() {
      return "DELETE";
    }
  }

  static final class AppendOperation extends AbstractOperation {

    private final String value;

    AppendOperation(String value) {
      super(OperationType.Append);
      this.value = value;
    }

    @Override
    public int apply(Editor editor, StringBuilder textBuffer, int cursorPosition) {
      if (editor.hasSelection()) {
        int start = editor.selectionStart;
        int end = editor.selectionEnd;
        editor.clearSelection();
      }
      textBuffer.insert(cursorPosition, value);
      return textBuffer.length();
    }

    @Override
    public String getContents() {
      return value;
    }
  }

  static final class MoveOperation extends AbstractOperation {

    private final int position;

    public MoveOperation(String position) {
      super(OperationType.Move);
      this.position = Integer.parseInt(position);
    }

    @Override
    public int apply(Editor editor, StringBuilder textBuffer, int cursorPosition) {
      if (position < 0) {
        return 0;
      }
      if (position > textBuffer.length()) {
        return textBuffer.length();
      }
      return position;
    }

    @Override
    public String getContents() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean includeInHistory() {
      return false;
      //      return super.includeInHistory();
    }
  }

  public static String[] edit(String[][] queries) {

    Editor editor = new Editor();
    for (String[] query : queries) {
      String operationName = query[0];
      OperationType opType = OperationType.parse(operationName);
      switch (opType) {
        case Append:
          editor.apply(new AppendOperation(query[1]));
          break;
        case Move:
          editor.apply(new MoveOperation(query[1]));
          break;
        case Delete:
          editor.apply(new DeleteOperation());
          break;
        case Select:
          editor.apply(new SelectOperation(query[1], query[2]));
      }
    }
    return editor.getHistoryAsString();
  }

  String[] textEditor2_2(String[][] queries) {
    return edit(queries);
  }
}
