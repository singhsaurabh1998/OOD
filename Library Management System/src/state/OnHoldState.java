package state;


import models.BookCopy;
import models.Member;
import service.TransactionService;

/*The `HoldState` (often called `OnHoldState`) is a state in the State pattern used for `BookCopy`.
It represents a situation where a book copy is reserved (on hold) for a member, so it cannot be checked out by others until
the hold is resolved or expires. In this state:
- The copy is not available for general checkout.
- Only the member with the hold may be allowed to check it out.
- Actions like `checkout`, `returnItem`, and `placeHold` behave differently, often restricting access or updating the state.

This helps manage reservations and ensures fair access to popular items in the library system.

 */
public class OnHoldState implements ItemState {
    @Override
    public void checkout(BookCopy copy, Member member) {
        // Only a member who placed the hold can check it out.
        if (copy.getItem().isObserver(member)) {
            TransactionService.getInstance().createLoan(copy, member);
            copy.getItem().removeObserver(member); // Remove from waiting list
            copy.setState(new CheckedOutState());
            System.out.println("Hold fulfilled. " + copy.getId() + " checked out by " + member.getName());
        } else {
            System.out.println("This item is on hold for another member.");
        }
    }

    @Override
    public void returnItem(BookCopy c) {
        System.out.println("Invalid action. Item is on hold, not checked out.");
    }

    @Override
    public void placeHold(BookCopy c, Member m) {
        System.out.println("Item is already on hold.");
    }
}
