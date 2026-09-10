package com.system.service;

import com.system.model.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EligibilityEvaluatorTest {

    private final EligibilityEvaluator evaluator = new EligibilityEvaluator();

    @Test
    public void testNormalScenario_Eligible() {
        Employee emp = new Employee("E001", "Alice", 28, "IT", "Active", 3, true);
        var result = evaluator.evaluateAccess(emp, 2);
        assertEquals("Eligible", result.status);
        assertTrue(result.reasons.isEmpty());
    }

    @Test
    public void testBoundaryScenario_AgeExactly21() {
        Employee emp = new Employee("E002", "Bob", 21, "HR", "Active", 2, true);
        var result = evaluator.evaluateAccess(emp, 2);
        assertEquals("Eligible", result.status);
    }

    @Test
    public void testConditionallyEligible_LowClearance() {
        Employee emp = new Employee("E003", "Charlie", 30, "Finance", "Active", 1, true);
        var result = evaluator.evaluateAccess(emp, 3);
        assertEquals("Conditionally Eligible", result.status);
        assertEquals(1, result.reasons.size());
    }

    @Test
    public void testMultipleFailuresScenario() {
        // Underage (19), Unauthorized Department (Marketing), Inactive status
        Employee emp = new Employee("E004", "David", 19, "Marketing", "Terminated", 2, false);
        var result = evaluator.evaluateAccess(emp, 1);
        
        assertEquals("Not Eligible", result.status);
        // Expecting 4 distinct failure errors captured together
        assertEquals(4, result.reasons.size());
    }

    @Test
    public void testInvalidInput_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> {
            evaluator.evaluateAccess(null, 1);
        });
    }
}
