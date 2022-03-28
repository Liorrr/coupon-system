package com.lior;

import com.lior.exceptions.MissingGeneratedKeysException;
import com.lior.tests.Tests;


public class Main {

    public static void main(String[] args) throws Exception, MissingGeneratedKeysException {
        Tests.TestAll();
    }
}
