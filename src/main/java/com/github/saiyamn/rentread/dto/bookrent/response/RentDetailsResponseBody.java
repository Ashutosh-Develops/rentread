package com.github.saiyamn.rentread.dto.bookrent.response;

import com.github.saiyamn.rentread.entity.Rent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentDetailsResponseBody {

  private long rentId;
  private UserInfo userInfo;
  private BookInfo bookInfo;

  public RentDetailsResponseBody(Rent rent){
      this.rentId=rent.getId();
      this.userInfo=UserInfo.getUserInfoInstance(rent.getUser());
      this.bookInfo=BookInfo.getBookInfoInstance(rent.getBook());
  }
}
