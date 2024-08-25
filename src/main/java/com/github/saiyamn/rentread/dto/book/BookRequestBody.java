package com.github.saiyamn.rentread.dto.book;

import com.github.saiyamn.rentread.entity.AvailabilityStatus;
import com.github.saiyamn.rentread.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookRequestBody {
    private String title;
    private String author;
    private String genre;
    private String availabilityStatus;
}
