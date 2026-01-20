package com.github.scoolfx.converter;

import com.github.scoolfx.exception.NepaliCalendarException;
import com.github.scoolfx.model.BsDate;
import com.github.scoolfx.model.BsMonth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NepaliDateConverterTest {

    private NepaliDateConverter converter;

    @BeforeEach
    void setUp() {
        converter = new NepaliDateConverter();
    }


    @Nested
    @DisplayName("Historical Nepali Events Conversion")
    class HistoricalEventTests {

        @ParameterizedTest(name = "{4} ({0}-{1}-{2} BS)")
        @CsvSource({
                // BS Year, BS Month (1-12), BS Day, AD Date, Event Name
                "2002, 8,  14, 1945-11-29, Padma Shumsher becomes PM",
                "2002, 10, 12, 1946-01-25, Formation of Nepali National Congress",
                "2005, 1,  18, 1948-04-30, Mohan Shumsher becomes last Rana PM",
                "2006, 12, 27, 1950-04-09, Formation of Nepali Congress",
                "2007, 7,  22, 1950-11-07, King Tribhuvan flees to India",
                "2007, 11, 7,  1951-02-18, Democracy Day (End of Rana Regime)",
                "2017, 9,  1,  1960-12-15, Royal Coup (Panchayat Begins)",
                "2037, 9,  1,  1980-12-15, National Referendum",
                "2046, 12, 26, 1990-04-08, Restoration of Multi-party Democracy",
                "2052, 11, 1,  1996-02-13, Start of Maoist People's War",
                "2058, 2,  19, 2001-06-01, Royal Palace Massacre",
                "2061, 10, 19, 2005-02-01, King Gyanendra absolute power",
                "2063, 1,  11, 2006-04-24, Jana Andolan II Success",
                "2063, 8,  5,  2006-11-21, Comprehensive Peace Accord",
                "2065, 2,  15, 2008-05-28, Federal Democratic Republic Declaration",
                "2072, 1,  12, 2015-04-25, Gorkha Earthquake",
                "2072, 6,  3,  2015-09-20, Promulgation of current Constitution"
        })
        void verifyHistoricalEvents(int bsYear, int bsMonth, int bsDay, String adDateStr, String eventName) {
            LocalDate expectedAd = LocalDate.parse(adDateStr);
            BsDate expectedBs = new BsDate(bsYear, BsMonth.fromValue(bsMonth), bsDay);

            // Test AD -> BS
            BsDate actualBs = converter.toBs(expectedAd);
            assertEquals(expectedBs, actualBs, "Failed AD to BS for: " + eventName);

            // Test BS -> AD
            LocalDate actualAd = converter.toAd(expectedBs);
            assertEquals(expectedAd, actualAd, "Failed BS to AD for: " + eventName);
        }
    }


    @Nested
    @DisplayName("Boundary Tests (First and Last Days)")
    class BoundaryTests {

        @Test
        @DisplayName("First day of the supported range (BS 2000-01-01)")
        void testFirstDayOfRange() {
            LocalDate startAd = LocalDate.of(1943, 4, 14);
            BsDate startBs = new BsDate(2000, BsMonth.BAISAKH, 1);

            assertEquals(startBs, converter.toBs(startAd));
            assertEquals(startAd, converter.toAd(startBs));
        }

        @Test
        @DisplayName("Last day of the supported range (BS 2085-12-31)")
        void testLastDayOfRange() {
            LocalDate endAd = LocalDate.of(2029, 4, 13);
            BsDate endBs = new BsDate(2085, BsMonth.CHAITRA, 31);

            assertEquals(endBs, converter.toBs(endAd));
            assertEquals(endAd, converter.toAd(endBs));
        }

        @Test
        @DisplayName("getMinSupportedAdDate should return first supported AD date")
        void testGetMinSupportedAdDate() {
            LocalDate expectedMin = LocalDate.of(1943, 4, 14);
            LocalDate actualMin = converter.getMinSupportedAdDate();
            assertEquals(expectedMin, actualMin, "Min supported AD date should be April 14, 1943");
        }

        @Test
        @DisplayName("getMaxSupportedAdDate should return last supported AD date")
        void testGetMaxSupportedAdDate() {
            LocalDate expectedMax = LocalDate.of(2029, 4, 13);
            LocalDate actualMax = converter.getMaxSupportedAdDate();
            assertEquals(expectedMax, actualMax, "Max supported AD date should be April 13, 2029");
        }

    }

    @Nested
    @DisplayName("AD New Year (Jan 1) vs BS Poush Mapping")
    class AdNewYearTests {

        @ParameterizedTest(name = "Jan 1, {0} AD <-> Poush {2}, {1} BS")
        @CsvSource({
                "1944, 2000, 17", "1945, 2001, 18", "1946, 2002, 18", "1947, 2003, 17", "1948, 2004, 17",
                "1949, 2005, 18", "1950, 2006, 18", "1951, 2007, 17", "1952, 2008, 17", "1953, 2009, 18",
                "1954, 2010, 18", "1955, 2011, 17", "1956, 2012, 17", "1957, 2013, 18", "1958, 2014, 18",
                "1959, 2015, 17", "1960, 2016, 17", "1961, 2017, 18", "1962, 2018, 18", "1963, 2019, 17",
                "1964, 2020, 17", "1965, 2021, 18", "1966, 2022, 17", "1967, 2023, 17", "1968, 2024, 17",
                "1969, 2025, 18", "1970, 2026, 17", "1971, 2027, 17", "1972, 2028, 17", "1973, 2029, 18",
                "1974, 2030, 17", "1975, 2031, 17", "1976, 2032, 17", "1977, 2033, 18", "1978, 2034, 17",
                "1979, 2035, 17", "1980, 2036, 17", "1981, 2037, 18", "1982, 2038, 17", "1983, 2039, 17",
                "1984, 2040, 17", "1985, 2041, 18", "1986, 2042, 17", "1987, 2043, 17", "1988, 2044, 17",
                "1989, 2045, 18", "1990, 2046, 17", "1991, 2047, 17", "1992, 2048, 17", "1993, 2049, 17",
                "1994, 2050, 17", "1995, 2051, 17", "1996, 2052, 17", "1997, 2053, 17", "1998, 2054, 17",
                "1999, 2055, 17", "2000, 2056, 17", "2001, 2057, 17", "2002, 2058, 17", "2003, 2059, 17",
                "2004, 2060, 17", "2005, 2061, 17", "2006, 2062, 17", "2007, 2063, 17", "2008, 2064, 17",
                "2009, 2065, 17", "2010, 2066, 17", "2011, 2067, 17", "2012, 2068, 17", "2013, 2069, 17",
                "2014, 2070, 17", "2015, 2071, 17", "2016, 2072, 17", "2017, 2073, 17", "2018, 2074, 17",
                "2019, 2075, 17", "2020, 2076, 16", "2021, 2077, 17", "2022, 2078, 17", "2023, 2079, 17",
                "2024, 2080, 16", "2025, 2081, 17", "2026, 2082, 17", "2027, 2083, 17", "2028, 2084, 16",
                "2029, 2085, 17"
        })
        void verifyNewYearMapping(int adYear, int bsYear, int bsDay) {
            LocalDate adDate = LocalDate.of(adYear, 1, 1);
            BsDate expectedBsDate = new BsDate(bsYear, BsMonth.POUSH, bsDay);

            // Test AD to BS
            assertEquals(expectedBsDate, converter.toBs(adDate),
                    String.format("AD to BS failed: Jan 1, %d should be Poush %d, %d BS", adYear, bsDay, bsYear));

            // Test BS to AD
            assertEquals(adDate, converter.toAd(expectedBsDate),
                    String.format("BS to AD failed: Poush %d, %d BS should be Jan 1, %d AD", bsDay, bsYear, adYear));
        }
    }

    @Nested
    @DisplayName("AD Year End (Dec 31) vs BS Poush Mapping")
    class AdYearEndTests {

        @ParameterizedTest(name = "Dec 31, {0} AD <-> Poush {2}, {1} BS")
        @CsvSource({
                "1943, 2000, 16", "1944, 2001, 17", "1945, 2002, 17", "1946, 2003, 16", "1947, 2004, 16",
                "1948, 2005, 17", "1949, 2006, 17", "1950, 2007, 16", "1951, 2008, 16", "1952, 2009, 17",
                "1953, 2010, 17", "1954, 2011, 16", "1955, 2012, 16", "1956, 2013, 17", "1957, 2014, 17",
                "1958, 2015, 16", "1959, 2016, 16", "1960, 2017, 17", "1961, 2018, 17", "1962, 2019, 16",
                "1963, 2020, 16", "1964, 2021, 17", "1965, 2022, 16", "1966, 2023, 16", "1967, 2024, 16",
                "1968, 2025, 17", "1969, 2026, 16", "1970, 2027, 16", "1971, 2028, 16", "1972, 2029, 17",
                "1974, 2031, 16", "1975, 2032, 16", "1976, 2033, 17", "1977, 2034, 16", "1978, 2035, 16",
                "1979, 2036, 16", "1980, 2037, 17", "1981, 2038, 16", "1982, 2039, 16", "1983, 2040, 16",
                "1984, 2041, 17", "1985, 2042, 16", "1986, 2043, 16", "1987, 2044, 16", "1988, 2045, 17",
                "1989, 2046, 16", "1990, 2047, 16", "1991, 2048, 16", "1992, 2049, 16", "1993, 2050, 16",
                "1994, 2051, 16", "1995, 2052, 16", "1996, 2053, 16", "1997, 2054, 16", "1998, 2055, 16",
                "1999, 2056, 16", "2000, 2057, 16", "2001, 2058, 16", "2002, 2059, 16", "2003, 2060, 16",
                "2004, 2061, 16", "2005, 2062, 16", "2006, 2063, 16", "2007, 2064, 16", "2008, 2065, 16",
                "2009, 2066, 16", "2010, 2067, 16", "2011, 2068, 16", "2012, 2069, 16", "2013, 2070, 16",
                "2014, 2071, 16", "2015, 2072, 16", "2016, 2073, 16", "2017, 2074, 16", "2018, 2075, 16",
                "2019, 2076, 15", "2020, 2077, 16", "2021, 2078, 16", "2022, 2079, 16", "2023, 2080, 15",
                "2024, 2081, 16", "2025, 2082, 16", "2026, 2083, 16", "2027, 2084, 15", "2028, 2085, 16"
        })
        void verifyYearEndMapping(int adYear, int bsYear, int bsDay) {
            LocalDate adDate = LocalDate.of(adYear, 12, 31);
            BsDate expectedBsDate = new BsDate(bsYear, BsMonth.POUSH, bsDay);

            // 1. AD to BS
            assertEquals(expectedBsDate, converter.toBs(adDate),
                    String.format("AD to BS failed: Dec 31, %d should be Poush %d, %d BS", adYear, bsDay, bsYear));

            // 2. BS to AD (The Opposite Way)
            assertEquals(adDate, converter.toAd(expectedBsDate),
                    String.format("BS to AD failed: Poush %d, %d BS should be Dec 31, %d AD", bsDay, bsYear, adYear));
        }
    }

    @Nested
    @DisplayName("BS New Year (Baisakh 1) vs AD Date Mapping")
    class BsNewYearBoundaryTests {

        @ParameterizedTest(name = "Baisakh 1, {0} BS <-> {1} AD")
        @CsvSource({
                "2000, 1943-04-14", "2001, 1944-04-13", "2002, 1945-04-13", "2003, 1946-04-13", "2004, 1947-04-14",
                "2005, 1948-04-13", "2006, 1949-04-13", "2007, 1950-04-13", "2008, 1951-04-14", "2009, 1952-04-13",
                "2010, 1953-04-13", "2011, 1954-04-13", "2012, 1955-04-14", "2013, 1956-04-13", "2014, 1957-04-13",
                "2015, 1958-04-13", "2016, 1959-04-14", "2017, 1960-04-13", "2018, 1961-04-13", "2019, 1962-04-13",
                "2020, 1963-04-14", "2021, 1964-04-13", "2022, 1965-04-13", "2023, 1966-04-13", "2024, 1967-04-14",
                "2025, 1968-04-13", "2026, 1969-04-13", "2027, 1970-04-14", "2028, 1971-04-14", "2029, 1972-04-13",
                "2030, 1973-04-13", "2031, 1974-04-14", "2032, 1975-04-14", "2033, 1976-04-13", "2034, 1977-04-13",
                "2035, 1978-04-14", "2036, 1979-04-14", "2037, 1980-04-13", "2038, 1981-04-13", "2039, 1982-04-14",
                "2040, 1983-04-14", "2041, 1984-04-13", "2042, 1985-04-13", "2043, 1986-04-14", "2044, 1987-04-14",
                "2045, 1988-04-13", "2046, 1989-04-13", "2047, 1990-04-14", "2048, 1991-04-14", "2049, 1992-04-13",
                "2050, 1993-04-13", "2051, 1994-04-14", "2052, 1995-04-14", "2053, 1996-04-13", "2054, 1997-04-13",
                "2055, 1998-04-14", "2056, 1999-04-14", "2057, 2000-04-13", "2058, 2001-04-14", "2059, 2002-04-14",
                "2060, 2003-04-14", "2061, 2004-04-13", "2062, 2005-04-14", "2063, 2006-04-14", "2064, 2007-04-14",
                "2065, 2008-04-13", "2066, 2009-04-14", "2067, 2010-04-14", "2068, 2011-04-14", "2069, 2012-04-13",
                "2070, 2013-04-14", "2071, 2014-04-14", "2072, 2015-04-14", "2073, 2016-04-13", "2074, 2017-04-14",
                "2075, 2018-04-14", "2076, 2019-04-14", "2077, 2020-04-13", "2078, 2021-04-14", "2079, 2022-04-14",
                "2080, 2023-04-14", "2081, 2024-04-13", "2082, 2025-04-14", "2083, 2026-04-14", "2084, 2027-04-14",
                "2085, 2028-04-14"
        })
        void verifyBsNewYearMapping(int bsYear, String adDateStr) {
            BsDate bsDate = new BsDate(bsYear, BsMonth.BAISAKH, 1);
            LocalDate expectedAdDate = LocalDate.parse(adDateStr);

            // 1. BS to AD
            LocalDate actualAdDate = converter.toAd(bsDate);
            assertEquals(expectedAdDate, actualAdDate,
                    String.format("BS to AD failed: Baisakh 1, %d BS should be %s AD", bsYear, adDateStr));

            // 2. AD to BS
            BsDate actualBsDate = converter.toBs(expectedAdDate);
            assertEquals(bsDate, actualBsDate,
                    String.format("AD to BS failed: %s AD should be Baisakh 1, %d BS", adDateStr, bsYear));
        }
    }

    @Nested
    @DisplayName("BS Year End (Chaitra Last Day) vs AD Date Mapping")
    class BsYearEndBoundaryTests {

        @ParameterizedTest(name = "Chaitra {1}, {0} BS <-> {2} AD")
        @CsvSource({
                "2000, 31, 1944-04-12", "2001, 30, 1945-04-12", "2002, 30, 1946-04-12", "2003, 31, 1947-04-13", "2004, 31, 1948-04-12",
                "2005, 30, 1949-04-12", "2006, 30, 1950-04-12", "2007, 31, 1951-04-13", "2008, 31, 1952-04-12", "2009, 30, 1953-04-12",
                "2010, 30, 1954-04-12", "2011, 31, 1955-04-13", "2012, 30, 1956-04-12", "2013, 30, 1957-04-12", "2014, 30, 1958-04-12",
                "2015, 31, 1959-04-13", "2016, 30, 1960-04-12", "2017, 30, 1961-04-12", "2018, 30, 1962-04-12", "2019, 31, 1963-04-13",
                "2020, 30, 1964-04-12", "2021, 30, 1965-04-12", "2022, 30, 1966-04-12", "2023, 31, 1967-04-13", "2024, 30, 1968-04-12",
                "2025, 30, 1969-04-12", "2026, 31, 1970-04-13", "2027, 31, 1971-04-13", "2028, 30, 1972-04-12", "2029, 30, 1973-04-12",
                "2030, 31, 1974-04-13", "2031, 31, 1975-04-13", "2032, 30, 1976-04-12", "2033, 30, 1977-04-12", "2034, 31, 1978-04-13",
                "2035, 31, 1979-04-13", "2036, 30, 1980-04-12", "2037, 30, 1981-04-12", "2038, 31, 1982-04-13", "2039, 30, 1983-04-13",
                "2040, 30, 1984-04-12", "2041, 30, 1985-04-12", "2042, 31, 1986-04-13", "2043, 30, 1987-04-13", "2044, 30, 1988-04-12",
                "2045, 30, 1989-04-12", "2046, 31, 1990-04-13", "2047, 30, 1991-04-13", "2048, 30, 1992-04-12", "2049, 30, 1993-04-12",
                "2050, 31, 1994-04-13", "2051, 30, 1995-04-13", "2052, 30, 1996-04-12", "2053, 30, 1997-04-12", "2054, 31, 1998-04-13",
                "2055, 30, 1999-04-13", "2056, 30, 2000-04-12", "2057, 31, 2001-04-13", "2058, 31, 2002-04-13", "2059, 30, 2003-04-13",
                "2060, 30, 2004-04-12", "2061, 31, 2005-04-13", "2062, 31, 2006-04-13", "2063, 30, 2007-04-13", "2064, 30, 2008-04-12",
                "2065, 31, 2009-04-13", "2066, 31, 2010-04-13", "2067, 30, 2011-04-13", "2068, 30, 2012-04-12", "2069, 31, 2013-04-13",
                "2070, 30, 2014-04-13", "2071, 30, 2015-04-13", "2072, 30, 2016-04-12", "2073, 31, 2017-04-13", "2074, 30, 2018-04-13",
                "2075, 30, 2019-04-13", "2076, 30, 2020-04-12", "2077, 31, 2021-04-13", "2078, 30, 2022-04-13", "2079, 30, 2023-04-13",
                "2080, 30, 2024-04-12", "2081, 31, 2025-04-13", "2082, 30, 2026-04-13", "2083, 30, 2027-04-13", "2084, 31, 2028-04-13",
                "2085, 31, 2029-04-13"
        })
        void verifyBsYearEndMapping(int bsYear, int chaitraDay, String adDateStr) {
            BsDate bsDate = new BsDate(bsYear, BsMonth.CHAITRA, chaitraDay);
            LocalDate expectedAdDate = LocalDate.parse(adDateStr);

            // 1. BS to AD
            LocalDate actualAdDate = converter.toAd(bsDate);
            assertEquals(expectedAdDate, actualAdDate,
                    String.format("BS to AD failed for %d BS: Chaitra %d should be %s AD", bsYear, chaitraDay, adDateStr));

            // 2. AD to BS
            BsDate actualBsDate = converter.toBs(expectedAdDate);
            assertEquals(bsDate, actualBsDate,
                    String.format("AD to BS failed for %s AD: Should be Chaitra %d, %d BS", adDateStr, chaitraDay, bsYear));
        }
    }

    @Nested
    @DisplayName("Exception and Error Handling")
    class ExceptionTests {

        @Test
        @DisplayName("Should throw OUT_OF_RANGE for AD dates before 1943")
        void testUnderRangeAd() {
            LocalDate tooEarly = LocalDate.of(1943, 4, 13);
            NepaliCalendarException ex = assertThrows(NepaliCalendarException.class, () -> converter.toBs(tooEarly));
            assertEquals(NepaliCalendarException.ErrorCode.OUT_OF_RANGE, ex.getErrorCode());
        }

        @Test
        @DisplayName("Should throw OUT_OF_RANGE for AD dates after data range")
        void testOverRangeAd() {
            LocalDate tooLate = LocalDate.of(2045, 1, 1);
            NepaliCalendarException ex = assertThrows(NepaliCalendarException.class, () -> converter.toBs(tooLate));
            assertEquals(NepaliCalendarException.ErrorCode.OUT_OF_RANGE, ex.getErrorCode());
        }

        @Test
        @DisplayName("Should throw INVALID_BS_DATE for impossible days (e.g., Baisakh 32 when max is 31)")
        void testInvalidBsDay() {
            BsDate invalidBs = new BsDate(2082, BsMonth.BAISAKH, 32);
            NepaliCalendarException ex = assertThrows(NepaliCalendarException.class, () -> converter.toAd(invalidBs));
            assertEquals(NepaliCalendarException.ErrorCode.INVALID_BS_DATE, ex.getErrorCode());
        }
    }
}