package aine;

import java.io.File;
import java.io.IOException;

import aine.dex.Dex;
import aine.dex.DexClass;
import aine.dex.DexDump;

/**
 * Entry point. With a file argument, dumps its classes; otherwise prints usage.
 */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Aine: run Android apps on desktop (scaffold). Usage: aine.Main <dex>");
            return;
        }
        try {
            Dex dex = Dex.load(new File(args[0]));
            for (DexClass cls : dex.classes()) {
                System.out.print(DexDump.dump(cls));
            }
        } catch (IOException e) {
            System.err.println("cannot read dex: " + e.getMessage());
            System.exit(1);
        }
    }
}
