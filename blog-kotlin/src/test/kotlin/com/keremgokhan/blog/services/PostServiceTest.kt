package com.keremgokhan.blog.services

import com.keremgokhan.blog.models.Posts
import com.keremgokhan.blog.models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PostServiceTest {
    private lateinit var postService: PostService
    private lateinit var userService: UserService
    private var testUserId: Int = 0

    @BeforeEach
    fun setup() {
        // Use in-memory H2 database for testing
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users, Posts)
        }
        postService = PostService()
        userService = UserService()

        // Create test user
        val user = userService.createUser("testauthor", "password123")!!
        testUserId = user.id
    }

    @AfterEach
    fun teardown() {
        transaction {
            SchemaUtils.drop(Posts, Users)
        }
    }

    @Test
    fun `createPost should create a post`() {
        val post = postService.createPost("Test Title", "Test Body", testUserId)

        assertNotNull(post)
        assertEquals("Test Title", post.title)
        assertEquals("Test Body", post.body)
        assertEquals(testUserId, post.authorId)
    }

    @Test
    fun `getPostById should return post when exists`() {
        val createdPost = postService.createPost("Test Title", "Test Body", testUserId)!!

        val post = postService.getPostById(createdPost.id)

        assertNotNull(post)
        assertEquals(createdPost.id, post.id)
        assertEquals("Test Title", post.title)
        assertEquals("testauthor", post.author)
    }

    @Test
    fun `getPostById should return null when post does not exist`() {
        val post = postService.getPostById(999)

        assertNull(post)
    }

    @Test
    fun `getAllPosts should return all posts in descending order`() {
        postService.createPost("First Post", "Body 1", testUserId)
        Thread.sleep(10) // Ensure different timestamps
        postService.createPost("Second Post", "Body 2", testUserId)

        val posts = postService.getAllPosts()

        assertEquals(2, posts.size)
        assertEquals("Second Post", posts[0].title) // Most recent first
        assertEquals("First Post", posts[1].title)
    }

    @Test
    fun `deletePost should remove post`() {
        val post = postService.createPost("Test Title", "Test Body", testUserId)!!

        val deleted = postService.deletePost(post.id)

        assertTrue(deleted)
        assertNull(postService.getPostById(post.id))
    }

    @Test
    fun `updatePost should modify post`() {
        val post = postService.createPost("Original Title", "Original Body", testUserId)!!

        val updated = postService.updatePost(post.id, "Updated Title", "Updated Body")

        assertTrue(updated)
        val updatedPost = postService.getPostById(post.id)
        assertNotNull(updatedPost)
        assertEquals("Updated Title", updatedPost.title)
        assertEquals("Updated Body", updatedPost.body)
    }
}
