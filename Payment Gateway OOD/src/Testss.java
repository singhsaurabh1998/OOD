import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Testss  {

    public static void main(String[] args) {

       // HashSet<Integer>hs = new HashSet<>();
        List<Integer>list = List.of(1, 2, 3, 5, 6, 7, 8, 9, 10);

        int n = list.size()+1;
        int sum = n*(n+1)/2;
        int currSum = 0;
//        if(list.isEmpty())
//        {
//            System.out.println("list is empty");
//        }
        for (int ele:list)
            currSum+=ele;

        int ans = sum-currSum;
        System.out.println("missing number is : "+ans);

//        int missingNum = 0;
//        for(int i = 1;i<=list.size()+1;i++)
//        {
//            if(!hs.contains(i))
//            {
//                System.out.println(i+ " is the missing number ");
//                break;
//            }
//        }

////       1 2 4 3 5
////            size+1
//       hm =  {1:1, 2:1, 3:1, 4:1 5:1 } SC = O(n)
        //TC = O(N)
//        int,int
//                1 to 6
//            i = 6
    }
}