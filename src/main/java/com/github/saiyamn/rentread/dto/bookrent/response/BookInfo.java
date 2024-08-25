package com.github.saiyamn.rentread.dto.bookrent.response;

import com.github.saiyamn.rentread.entity.Book;
import com.github.saiyamn.rentread.entity.Genre;
import com.github.saiyamn.rentread.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookInfo {

    private long bookId;
    private String title;
    private String author;
    private Genre genre;


    private BookInfo(Book book){
        this.bookId=book.getId();
        this.title=book.getTitle();
        this.author=book.getAuthor();
        this.genre=book.getGenre();
    }

    public static BookInfo getBookInfoInstance(Book book){
        return new BookInfo(book);
    }

}
