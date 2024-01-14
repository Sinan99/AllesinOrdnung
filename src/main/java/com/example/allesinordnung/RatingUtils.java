package com.example.allesinordnung;
public class RatingUtils {

    public static boolean isValidRatingInput(String input) {
        try {
            if (input.matches("\\d+")) {
                double filterValue = Double.parseDouble(input);
                return filterValue >= 0 && filterValue <= 10;
            } else if (input.matches("[<>]=?\\d+")) {
                double filterValue = Double.parseDouble(input.substring(1));
                return filterValue >= 0 && filterValue <= 10;
            }
            return false;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return false;
        }
    }

    public static boolean compareRating(Double rating, String filter) {
        try {
            if (rating == null || rating < 0 || rating > 10) {
                // Exclude null ratings and those outside the range [0, 10]
                return false;
            }

            char operator;
            double filterValue;

            if (filter.matches("\\d+")) {
                // If the filter contains only digits (no operator), set the operator to '='
                operator = '=';
                filterValue = Double.parseDouble(filter);
            } else {
                // Extract operator and filter value
                operator = filter.charAt(0);
                filterValue = Double.parseDouble(filter.substring(1));
            }

            // Check if the filter value is within the range (0 to 10, inclusive)
            boolean excludeYear = (filterValue >= 0 && filterValue <= 10);

            // Adjust the comparison based on the operator
            switch (operator) {
                case '>':
                    return excludeYear && rating > filterValue;
                case '<':
                    return excludeYear && rating < filterValue;
                case '=':
                    return excludeYear && Math.floor(rating) == filterValue;
                default:
                    // If no operator is provided, default to '>=' comparison
                    return excludeYear && rating >= filterValue;
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            // Handle the case where the input is not a valid double or does not contain a valid operator
            return false;
        }
    }
}

