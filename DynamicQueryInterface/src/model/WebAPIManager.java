package model;


import helper.CalenderToolBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


public class WebAPIManager {
    private static WebAPIManager webAPIManager;
    private List<String> dates;
    private List<JSONObject> rates;
    private HashMap<String, ArrayList<Double>> ratesByCountry;
    private String url;
    private String baseCurrency;
    private LocalDate endDate;

    private WebAPIManager() {
        dates = new ArrayList<>();
        rates = new ArrayList<>();
        ratesByCountry = new HashMap<>();
        url = "https://api.exchangeratesapi.io/";
        baseCurrency = "USD";

        LocalDate today = LocalDate.now();
        endDate = today.minusDays(30);
        loadDataSet(endDate, today);
    }

    public static WebAPIManager getInstance() {
        if(webAPIManager == null) {
            webAPIManager = new WebAPIManager();
        }
        return webAPIManager;
    }

    private void loadDataSet(LocalDate fromDate, LocalDate toDate) {
        int dayWindow = (int)ChronoUnit.DAYS.between(fromDate,toDate);
        System.out.println("day inbetween: " + dayWindow);
        //set rates By Date
        try {
            for(int i = 0; i <= dayWindow; i++) {
                String urlWithPram = url + toDate.minusDays(i) + "?base=" + baseCurrency;
                InputStream is = new URL(urlWithPram).openStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int token;
                while((token = br.read()) != -1) {
                    sb.append((char)token);
                }
                JSONObject jo = new JSONObject(sb.toString());
                if(dates.indexOf(jo.get("date").toString()) == -1) {
                    dates.add(jo.get("date").toString());
                    System.out.println(jo.get("date").toString());
                    rates.add((JSONObject)jo.get("rates"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Set rates By Country
        String[] countries = getCountries();
        for(String s : countries) {
            ratesByCountry.put(s, new ArrayList<>());
        }
        rates.forEach(v -> {
            for(String s : countries) {
                try {
                    ratesByCountry.get(s).add(Double.parseDouble(v.get(s).toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void expendDataSet(LocalDate newEndDate) {
        if(dates.indexOf(newEndDate.toString()) == -1) {
            loadDataSet(newEndDate.minusDays(1),newEndDate);
            endDate = newEndDate;
        }
    }

    public void switchBaseCurrency(String country) {
        dates.clear();
        rates.clear();
        ratesByCountry.clear();
        baseCurrency = country;
        loadDataSet(endDate,LocalDate.now());
    }

    public LocalDate getEndDate() { return endDate; }
    public int getActualWindowSize() { return dates.size(); }
    public String getBaseCurrency() { return baseCurrency; }

    public JSONObject getAllRatesOf(LocalDate key) throws JSONException {
        if(key.getDayOfWeek().toString().equals("SATURDAY")) {
            return rates.get(dates.indexOf(key.minusDays(1).toString()));
        } else if(key.getDayOfWeek().toString().equals("SUNDAY")) {
            return rates.get(dates.indexOf(key.minusDays(2).toString()));
        }
        return rates.get(dates.indexOf(key.toString()));
    }

    public String[] getCountries() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(JSONObject.getNames(rates.get(0))));
        list.remove(baseCurrency);
        return list.stream().toArray(String[]::new);
    }

    public String[] getAllCountries() {
        return JSONObject.getNames(rates.get(0));
    }

    public String[] getDates(LocalDate fromDate, LocalDate toDate) {
        int begin = 0;
        int end = 0;
        if(fromDate.getDayOfWeek().toString().equals("SATURDAY")) {
            end = dates.indexOf(fromDate.plusDays(2).toString());
        } else if(fromDate.getDayOfWeek().toString().equals("SUNDAY")) {
            end = dates.indexOf(fromDate.plusDays(1).toString());
        } else {
            end = dates.indexOf(fromDate.toString());
        }
        if(toDate.getDayOfWeek().toString().equals("SATURDAY")) {
            begin = dates.indexOf(toDate.minusDays(1).toString());
        } else if(toDate.getDayOfWeek().toString().equals("SUNDAY")) {
            begin = dates.indexOf(toDate.minusDays(2).toString());
        } else {
            begin = dates.indexOf(toDate.toString());
        }
        if(begin < 0) {
            begin = 0;
        }
        System.out.printf("begin %d, end %d\n", begin, end);
        String[] arr = new String[end-begin];
        for(int i = 0; i < end-begin; i++) {
            arr[i] = dates.get(begin + i);
        }
        return arr;
    }

    public int getIndexOfDates(String dateString) {
        return dates.indexOf(dateString);
    }

    public List<Double> getAllRatesByCountry(String country) {
        return ratesByCountry.get(country);
    }

}