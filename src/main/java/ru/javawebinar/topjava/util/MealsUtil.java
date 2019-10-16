package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
    );

        List<MealTo> mealsTo = getFiltered(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY);
        mealsTo.forEach(System.out::println);

        System.out.println(getFilteredByCycle(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY));
        System.out.println(getFilteredByRecursion(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY));
//        System.out.println(getFilteredByAtomic(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY));
//        System.out.println(getFilteredByClosure(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY));
        System.out.println(getFilteredByExecutor(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY));
        System.out.println(getFilteredByFlatMap(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY));
        System.out.println(getFilteredByCollector(meals, startTime, endTime, DEFAULT_CALORIES_PER_DAY));
    }

    public static List<MealTo> getFiltered(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );
        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}