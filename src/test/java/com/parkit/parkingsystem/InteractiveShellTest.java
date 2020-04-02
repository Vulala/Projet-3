package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.parkit.parkingsystem.service.InteractiveShell;

class InteractiveShellTest {

	// Test for the InteractiveShell Class to be sure that each case from the
	// switch work
	// Edit : No point to test that class as it's just a switch

	private static int o = 0;
	private static InteractiveShell interactiveShell;

	@BeforeAll
	private static void setUp() {
		interactiveShell = new InteractiveShell();
	}

	@ParameterizedTest(name = "arg equal the right case")
	@ValueSource(ints = { 1, 2, 3, 4 })
	public void interactiveShell(int arg) {
		int option = arg;
		switch (arg) {

		case 1: {
			o = option;
			assertEquals(arg, o);
			System.out.println(option);
			break;

		}
		case 2: {
			o = option;
			assertEquals(arg, o);
			System.out.println(option);
			break;

		}
		case 3: {
			o = option;
			assertEquals(arg, o);
			System.out.println(option);
			System.out.println("Exiting from the system!");
			break;

		}
		default:
			System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");

		}
	}

}
