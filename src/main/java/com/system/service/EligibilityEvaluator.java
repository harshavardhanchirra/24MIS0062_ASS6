package com.system.service;

import com.system.model.Employee;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EligibilityEvaluator {

    private static final List<String> AUTHORIZED_DEPARTMENTS = Arrays.asList("IT", "HR", "FINANCE", "ADMINISTRATION");

    public static class EvaluationResult {
        public String status; // Eligible, Conditionally Eligible, Not Eligible
        public List<String> reasons = new ArrayList<>();

        @Override
        public String toString() {
            return "Status: " + status + (reasons.isEmpty() ? "" : " | Reasons: " + reasons);
        }
    }

    public EvaluationResult evaluateAccess(Employee employee, int requiredResourceLevel) {
        EvaluationResult result = new EvaluationResult();
        
        // Input validation
        if (employee == null) {
            throw new IllegalArgumentException("Employee details cannot be null.");
        }

        // Rule checking
        if (employee.getAge() < 21) {
            result.reasons.add("Employee is under the minimum age requirement of 21 (Age: " + employee.getAge() + ").");
        }

        if (employee.getDepartment() == null || !AUTHORIZED_DEPARTMENTS.contains(employee.getDepartment().toUpperCase())) {
            result.reasons.add("Department '" + employee.getDepartment() + "' is not authorized.");
        }

        if (employee.getEmploymentType() == null || !employee.getEmploymentType().equalsIgnoreCase("ACTIVE")) {
            result.reasons.add("Employment status is inactive or invalid.");
        }

        if (!employee.isIdValid()) {
            result.reasons.add("Employee ID card is expired or invalid.");
        }

        // Evaluate core eligibility rules
        if (!result.reasons.isEmpty()) {
            result.status = "Not Eligible";
            return result;
        }

        // Check conditional eligibility (Security clearance)
        if (employee.getSecurityClearanceLevel() < requiredResourceLevel) {
            result.status = "Conditionally Eligible";
            result.reasons.add("Insufficient security clearance (Has: " + employee.getSecurityClearanceLevel() + ", Required: " + requiredResourceLevel + ").");
        } else {
            result.status = "Eligible";
        }

        return result;
    }
}
