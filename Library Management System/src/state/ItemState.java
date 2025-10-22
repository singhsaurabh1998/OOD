package state;


import models.BookCopy;
import models.Member;
// State interface for BookCopy states
public interface ItemState {
    void checkout(BookCopy copy, Member member);
    void returnItem(BookCopy copy);
    void placeHold(BookCopy copy, Member member);
}
