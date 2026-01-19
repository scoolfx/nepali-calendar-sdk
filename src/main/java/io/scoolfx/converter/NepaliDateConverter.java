package io.scoolfx.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.scoolfx.exception.NepaliCalendarException;
import io.scoolfx.model.*;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NepaliDateConverter {

    private final Map<Integer, InternalYearData> yearMap = new HashMap<>();
    private final NavigableMap<LocalDate, InternalYearData> adMap = new TreeMap<>();

    public NepaliDateConverter() {
        loadData();
    }

    private void loadData() {
        try (InputStream is = getClass().getResourceAsStream("/nepali-calendar-data.json")) {
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            InternalYearData[] data = mapper.readValue(is, InternalYearData[].class);
            for (var year : data) {
                yearMap.put(year.year(), year);
                adMap.put(year.startDate(), year);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Nepali Calendar Data", e);
        }
    }

    public BsDate toBs(LocalDate adDate) {
        var entry = adMap.floorEntry(adDate);

        if (entry == null || adDate.isAfter(entry.getValue().endDate())) {
            throw new NepaliCalendarException(
                    "AD Date " + adDate + " is outside the supported B.S. range.",
                    NepaliCalendarException.ErrorCode.OUT_OF_RANGE
            );
        }

        InternalYearData config = entry.getValue();
        int daysDiff = (int) ChronoUnit.DAYS.between(config.startDate(), adDate);

        for (int i = 0; i < 12; i++) {
            int mLen = config.monthLengths()[i];
            if (daysDiff < mLen) {
                return new BsDate(config.year(), BsMonth.fromValue(i + 1), daysDiff + 1);
            }
            daysDiff -= mLen;
        }

        throw new NepaliCalendarException("Logic error in date conversion", NepaliCalendarException.ErrorCode.INVALID_BS_DATE);
    }

    public LocalDate toAd(BsDate bsDate) {
        InternalYearData config = yearMap.get(bsDate.year());

        if (config == null) {
            throw new NepaliCalendarException(
                    "B.S. Year " + bsDate.year() + " is not supported by the current data file.",
                    NepaliCalendarException.ErrorCode.OUT_OF_RANGE
            );
        }

        if (bsDate.day() > config.getMonthLength(bsDate.month().getValue())) {
            throw new NepaliCalendarException(
                    "Invalid day " + bsDate.day() + " for month " + bsDate.month().getName(),
                    NepaliCalendarException.ErrorCode.INVALID_BS_DATE
            );
        }

        int daysOffset = bsDate.day() - 1;
        for (int i = 0; i < bsDate.month().getValue() - 1; i++) {
            daysOffset += config.monthLengths()[i];
        }
        return config.startDate().plusDays(daysOffset);
    }
}