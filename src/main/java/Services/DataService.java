package Services;

import DAO.DBRequest;
import DAO.RequestBin;
import DAO.UserDaysBin;
import Util.OperationResult;
import Util.PropertiesHolidaysUtil;
import Util.PropertiesSettingsUtil;
import Variable.ENUMS;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class DataService {

    private static final String MAX_YEAR_KEY = "vacation.max_year";
    private static final String MIN_YEAR_KEY = "vacation.min_year";
    private static final String DEFAULT_AVAILABLE_DAYS_KEY = "vacation.default_available_days";
    private static final String DEFAULT_AVAILABLE_PARTS_KEY = "vacation.default_available_parts";
    private static final String MINIMUM_DAYS_KEY = "vacation.minimum_days";
    private static final String CURRENT_YEAR_KEY = "vacation.current_year";

    public static OperationResult getYearsWithRequests(int userId, Set<RequestBin> requests){

        Set<UserDaysBin> years = new HashSet<>();

        for(int currentYear = PropertiesSettingsUtil.getInt(MIN_YEAR_KEY); currentYear < PropertiesSettingsUtil.getInt(MAX_YEAR_KEY) + 1; currentYear++) {
            OperationResult result = DBRequest.getUserDaysBin(userId, currentYear);
            if (result.getResult().equals(ENUMS.Result.REQUEST_ERROR)) return result;
            UserDaysBin userYear = (UserDaysBin) result.getBody();

            int availableDays = PropertiesSettingsUtil.getInt(DEFAULT_AVAILABLE_DAYS_KEY);
            int availableParts = PropertiesSettingsUtil.getInt(DEFAULT_AVAILABLE_PARTS_KEY);

            if (!result.getResult().equals(ENUMS.Result.NO_SUCH_YEAR_FOR_THIS_USER)) {
                availableDays = userYear.getDaysTotal();
                availableParts = userYear.getPartsTotal();
            }else {
                userYear.setYear(currentYear);
                userYear.setUserId(userId);
                userYear.setDaysTotal(PropertiesSettingsUtil.getInt(DEFAULT_AVAILABLE_DAYS_KEY));
                userYear.setPartsTotal(PropertiesSettingsUtil.getInt(DEFAULT_AVAILABLE_PARTS_KEY));
            }

            Set<RequestBin> yearRequests = new HashSet<>();

            for (RequestBin requestBin : requests) {
                if (requestBin.getStartDate().getYear() == currentYear) {
                    yearRequests.add(requestBin);
                    if (requestBin.getUserId() == userId) {
                        availableDays -= requestBin.getDuration();
                        availableParts--;
                    }
                }
            }

            userYear.setDaysAvailable(availableDays);
            userYear.setPartsAvailable(availableParts);
            userYear.setRequests(yearRequests);

            years.add(userYear);
            }

        return new OperationResult(ENUMS.Result.SUCCESS, years);
    }

    public static OperationResult getAvailableStatsInYear(int userId, int currentYear){
        OperationResult result = DBRequest.getAllRequestBinsForUser(userId);
        if (result.getResult().equals(ENUMS.Result.REQUEST_ERROR)) return result;
        Set<RequestBin> requests = (Set<RequestBin>) result.getBody();

        result = DBRequest.getUserDaysBin(userId, currentYear);
        if (result.getResult().equals(ENUMS.Result.REQUEST_ERROR)) return result;
        UserDaysBin userYear = (UserDaysBin) result.getBody();

        int availableDays = PropertiesSettingsUtil.getInt(DEFAULT_AVAILABLE_DAYS_KEY);
        int availableParts = PropertiesSettingsUtil.getInt(DEFAULT_AVAILABLE_PARTS_KEY);

        if (!result.getResult().equals(ENUMS.Result.NO_SUCH_YEAR_FOR_THIS_USER)) {
            availableDays = userYear.getDaysTotal();
            availableParts = userYear.getPartsTotal();
        }

        for (RequestBin requestBin : requests) {
            if (requestBin.getStartDate().getYear() == currentYear) {
                availableDays -= requestBin.getDuration();
                availableParts--;
            }
        }

        userYear.setDaysAvailable(availableDays);
        userYear.setPartsAvailable(availableParts);


        return new OperationResult(ENUMS.Result.SUCCESS, userYear);
    }

    public static OperationResult countDaysBetween(LocalDate startDate, LocalDate endDate) {

        if(startDate.isAfter(endDate)) return new OperationResult(ENUMS.Result.START_DATE_AFTER_END_DATE, null);

        if (isStarted(startDate)) return new OperationResult(ENUMS.Result.THAT_DAY_ALREADY_PASSED, null);

        long diff = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        for (LocalDate holiday: getHolidays(PropertiesHolidaysUtil.get())){
            if((endDate.isAfter(holiday) || endDate.equals(holiday)) && (startDate.isBefore(holiday) || startDate.equals(holiday))) diff--;
        }

        if (diff < PropertiesSettingsUtil.getInt(MINIMUM_DAYS_KEY)) return new OperationResult(ENUMS.Result.LESS_THEN_MINIMUM_DAYS, null);

        return new OperationResult (ENUMS.Result.SUCCESS, (int) diff);
    }

    public static boolean isStarted(LocalDate startDate) {
        return startDate.isBefore(LocalDate.now()) || startDate.isEqual(LocalDate.now());
    }

    public static boolean isFinished(LocalDate endDate) {
        return endDate.isBefore(LocalDate.now());
    }

    public static String getFormattedDateLocal(LocalDate dateLocal) {
        DecimalFormat dmFormat= new DecimalFormat("00");
        String mVal = dmFormat.format(Double.valueOf(dateLocal.getMonthValue()));
        String dVal = dmFormat.format(Double.valueOf(dateLocal.getDayOfMonth()));
        String yVal = String.valueOf(dateLocal.getYear());

        return dVal+mVal+yVal;
    }

    private static Set<LocalDate> getHolidays(Set<String> holidays) {
        Set<LocalDate> localDateSet = new HashSet<>();
        for (String day: holidays){
            String additionalYear = "";
            if(PropertiesHolidaysUtil.getType(day).equals("official")) additionalYear = PropertiesSettingsUtil.get(CURRENT_YEAR_KEY);
            localDateSet.add(parseDate(day + additionalYear));
        }
        return localDateSet;
    }

    private static LocalDate parseDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("ddMMyyyy"));
    }
}
