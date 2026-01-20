# Nepali Calendar SDK (Bikram Sambat)

A lightweight, high-precision Java library for converting dates between the Gregorian (A.D.) and Bikram Sambat (B.S.) calendar systems. Built for Java 21+.

## Features
- **Bi-directional Conversion**: Convert AD to BS and BS to AD.
- **Date Range Queries**: Get min/max supported AD dates.
- **High Precision**: Uses an internal JSON data source containing exact month lengths.
- **Type Safe**: Utilizes Java 21 Records and `java.time.LocalDate`.
- **Supported Range**: B.S. 2000 to B.S. 2085 (April 14, 1943 to April 13, 2029 AD).

## Installation

### 1. Add the JitPack repository to your build file
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### 2. Add Dependency to `pom.xml`

```xml

<dependencies>
    <dependency>
        <groupId>com.github.scoolfx</groupId>
        <artifactId>nepali-calendar-sdk</artifactId>
        <version>v1.0.1</version>
    </dependency>
</dependencies>
```

### Testing & Reliability
- Comprehensive unit tests using JUnit 5
- Covers core conversions, boundary dates (1943–2029), leap rules, invalid inputs


## Usage

### Initialize the Converter
```java
import com.github.scoolfx.converter.NepaliDateConverter;

NepaliDateConverter converter = new NepaliDateConverter();
```

### Convert A.D. to B.S.
```java
import java.time.LocalDate;
import com.github.scoolfx.model.BsDate;

LocalDate adDate = LocalDate.of(2024, 4, 13);
BsDate bsDate = converter.toBs(adDate);

System.out.println(bsDate.format()); // Output: 2081-01-01
System.out.println(bsDate.month().getName()); // Output: Baisakh
```

### Convert B.S. to A.D.
```java
import com.github.scoolfx.model.BsDate;
import com.github.scoolfx.model.BsMonth;
import java.time.LocalDate;

BsDate bsDate = new BsDate(2081, BsMonth.BAISAKH, 1);
LocalDate adDate = converter.toAd(bsDate);

System.out.println(adDate); // Output: 2024-04-13
```

### Get Supported Date Range
```java
import java.time.LocalDate;

LocalDate minDate = converter.getMinSupportedAdDate();
LocalDate maxDate = converter.getMaxSupportedAdDate();

System.out.println("Min supported AD date: " + minDate); // Output: 1943-04-14
System.out.println("Max supported AD date: " + maxDate); // Output: 2029-04-13
```

### Error Handling
The SDK throws `NepaliCalendarException` if a date is out of range or mathematically invalid.

```java
try {
    converter.toBs(LocalDate.of(1900, 1, 1));
} catch (NepaliCalendarException e) {
    System.out.println("Error: " + e.getErrorCode()); // Output: OUT_OF_RANGE
}
```
## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
```
