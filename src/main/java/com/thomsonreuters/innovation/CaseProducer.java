/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package com.thomsonreuters.innovation;

import javaslang.control.Try;
import org.apache.commons.cli.*;

public class CaseProducer {

    private static final String HELP = "h";

    private static final String TOPIC = "topic";

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder(TOPIC).hasArg().required().desc("The topic to which message will be published").build());
        options.addOption(HELP, "Show the help");

        CommandLineParser parser = new DefaultParser();
        CommandLine line = Try.of(() -> parser.parse(options, args)).onFailure(Throwable::getMessage).get();

        if (line.hasOption(HELP)) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "produce", options );
        }
    }
}
