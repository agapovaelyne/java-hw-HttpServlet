package repository;

import model.Post;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private AtomicLong counter;

    public Map<Long, Post> all() {
        return posts;
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Optional<Long> save(Post post) {
        long id = post.getId();
        if (id == 0) {
            id = counter.incrementAndGet();
            posts.put(id, post);
        } else {
            if (posts.get(post.getId()) != null) {
                posts.put(post.getId(), post);
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(id);
    }

    public Optional<Post> removeById(long id) {
        return Optional.ofNullable(posts.remove(id));
    }
}