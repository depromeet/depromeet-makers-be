package com.depromeet.makers.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Configuration
class MongoConfig {
    @Bean
    fun mappingMongoConverter(
        factory: MongoDatabaseFactory,
        context: MongoMappingContext,
        kstLocalDateTimeWriterConverter: LocalDateTimeToDateConverter,
        kstDateTimeReaderConverter: DateToLocalDateConverter,
        kstLocalDateTimeReaderConverter: DateToLocalDateTimeConverter,
        kstDateTimeWriterConverter: LocalDateToDateConverter,
    ) = MappingMongoConverter(DefaultDbRefResolver(factory), context).apply {
        setTypeMapper(DefaultMongoTypeMapper(null))
        customConversions =
            MongoCustomConversions(
                listOf(
                    kstLocalDateTimeWriterConverter,
                    kstLocalDateTimeReaderConverter,
                    kstDateTimeWriterConverter,
                    kstDateTimeReaderConverter,
                ),
            )
        afterPropertiesSet()
    }
}

@Component
@WritingConverter
class LocalDateTimeToDateConverter : Converter<LocalDateTime, Date> {
    override fun convert(source: LocalDateTime): Date = convertToKst(source)

    private fun convertToKst(localDateTime: LocalDateTime): Date = Timestamp.valueOf(localDateTime.plusHours(9))
}

@Component
@WritingConverter
class LocalDateToDateConverter : Converter<LocalDate, Date> {
    override fun convert(source: LocalDate): Date = convertToKst(source)

    private fun convertToKst(localDate: LocalDate): Date {
        val localDateTime = localDate.atStartOfDay().plusHours(9)
        return Timestamp.valueOf(localDateTime)
    }
}

@Component
@ReadingConverter
class DateToLocalDateTimeConverter : Converter<Date, LocalDateTime> {
    override fun convert(source: Date): LocalDateTime = convertToKst(source)

    private fun convertToKst(date: Date): LocalDateTime {
        val localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        return localDateTime.minusHours(9)
    }
}

@Component
@ReadingConverter
class DateToLocalDateConverter : Converter<Date, LocalDate> {
    override fun convert(source: Date): LocalDate = convertToKst(source)

    private fun convertToKst(date: Date): LocalDate {
        val localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        return localDateTime.minusHours(9).toLocalDate()
    }
}
