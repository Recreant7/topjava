package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetween;

public class UserMealsUtil {

    private static Map<Integer, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(23, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime,
                                                                   LocalTime endTime, int caloriesPerDay) {
        calculateCaloriesPerDay(mealList);
        return mealList.stream().filter(userMealWithExceed -> isBetween(userMealWithExceed.getDateTime()
                .toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExceed(userMeal, isExceedsCaloriesPerDay(userMeal, caloriesPerDay)))
                .collect(Collectors.toList());
    }

    private static void calculateCaloriesPerDay(List<UserMeal> mealList) {
        map = mealList.stream().collect(Collectors.groupingBy(o -> o.getDateTime().getDayOfMonth(), Collectors
                .summingInt(UserMeal::getCalories)));
    }

    private static boolean isExceedsCaloriesPerDay(UserMeal userMeal, int caloriesPerDay) {
        return map.get(userMeal.getDateTime().getDayOfMonth()).compareTo(caloriesPerDay) > 0;
    }
}
