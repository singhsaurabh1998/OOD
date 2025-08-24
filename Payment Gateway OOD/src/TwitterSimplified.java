import java.util.*;


class TwitterSimplified {
    public static int timeStamp = 0;
    Map<Integer, User> userMap;

    private class Tweet {
        public int id;
        public int time;
        public Tweet next;

        public Tweet(int id) {
            this.id = id;
            time = timeStamp++;
            next = null;
        }
    }

    private class User {
        public int userId;
        public Set<Integer> followed;
        public Tweet head;

        public User(int userId) {
            this.userId = userId;
            head = null;
            followed = new HashSet<>();
            follow(userId);
        }

        public void follow(int userId) {
            this.followed.add(userId);
        }

        public void unfollow(int userId) {
            this.followed.remove(userId);
        }

        public void post(int id) {
            Tweet t = new Tweet(id);
            t.next = head;
            head = t;
        }
    }

    public TwitterSimplified() {
        userMap = new HashMap<>();

    }

    public void postTweet(int userId, int tweetId) {
        if (!userMap.containsKey(userId)) { //user is not present
            User user = new User(userId);
            userMap.put(userId, user);
        }
        userMap.get(userId).post(tweetId);
    }

    public List<Integer> getNewsFeed(int userId) {
        List<Integer> res = new LinkedList<>();
        if (!userMap.containsKey(userId))
            return res;

        Set<Integer> followedUsers = userMap.get(userId).followed;
        PriorityQueue<Tweet> pq = new PriorityQueue<>((a, b) -> b.time - a.time);

        for (int id : followedUsers) {
            User u = userMap.get(id);
            Tweet head = u.head;
            if (head != null)
                pq.add(head);
        }

        int c = 0;
        while (!pq.isEmpty() && c < 10) {
            Tweet t = pq.poll();
            res.add(t.id);
            c++;
            if (t.next != null)
                pq.add(t.next);
        }
        return res;
    }

    public void follow(int followerId, int followeeId) {
        if (!userMap.containsKey(followerId)) {
            User u = new User(followerId);
            userMap.put(followerId, u);
        }
        if (!userMap.containsKey(followeeId)) {
            User u = new User(followeeId);
            userMap.put(followeeId, u);
        }
        userMap.get(followerId).follow(followeeId);
    }

    public void unfollow(int followerId, int followeeId) {
        if (!userMap.containsKey(followerId) || !userMap.containsKey(followeeId))
            return;

        userMap.get(followerId).unfollow(followeeId);
    }

    public static void main(String[] args) {
        TwitterSimplified twitter = new TwitterSimplified();
        twitter.postTweet(1, 5);
        System.out.println(twitter.getNewsFeed(1));
        twitter.follow(1, 2);    // User 1 follows user 2.
        twitter.postTweet(2, 6); // User 2 posts a new tweet (id = 6).
        System.out.println(twitter.getNewsFeed(1));  // User 1's news feed should return a list with 2 tweet ids -> [6, 5]. Tweet id 6 should precede tweet id 5 because it is posted after tweet id 5.
        twitter.unfollow(1, 2);  // User 1 unfollows user 2.
        System.out.println(twitter.getNewsFeed(1));  // User 1's news feed should return a list with 1 tweet id -> [5], since user 1 is no longer following user 2.
    }

}
