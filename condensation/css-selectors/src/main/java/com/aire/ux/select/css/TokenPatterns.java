package com.aire.ux.select.css;

import static java.lang.String.format;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings
interface TokenPatterns {

  String UNICODE = "\\\\[0-9a-f]{1,6}(\r\n|[ \n\r\t\f])?";

  String ESCAPE = format("%s|\\\\[^\n\r\f0-9a-f]", UNICODE);

  String NON_ASCII = "[^\0-\177]";

  String NAME_CHARACTER = format("[_a-z0-9-]|%s|%s", NON_ASCII, ESCAPE);

  String NAME_START = format("[_a-z]|%s|%s", NON_ASCII, ESCAPE);

  String NEWLINE = "\n|\r\n|\r|\f";

  String NUMBER = "\\d+(\\.?\\d+)*";

  String STRING_FORM_1 = format("\"([^\n\r\f\\\"]|\\\\%s|%s|%s)*\"", NEWLINE, NON_ASCII, ESCAPE);

  String STRING_FORM_2 = format("\'([^\n\r\f\\\"]|\\\\%s|%s|%s)*\'", NEWLINE, NON_ASCII, ESCAPE);

  String UNCLOSED_STRING_FORM_1 =
      format("\"([^\n\r\f\\\"]|\\\\%s|%s|%s)*", NEWLINE, NON_ASCII, ESCAPE);

  String UNCLOSED_STRING_FORM_2 =
      format("\'([^\n\r\f\\\"]|\\\\%s|%s|%s)*", NEWLINE, NON_ASCII, ESCAPE);

  String IDENTIFIER = format("[-]?(%s)(%s)*", NAME_START, NAME_CHARACTER);
}
