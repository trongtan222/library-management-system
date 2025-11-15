# N+1 Query Problem Fix

## Problem Description
The N+1 query problem occurs when:
- 1 query retrieves a parent entity (e.g., Books)
- N additional queries are executed to load related entities (e.g., Authors, Categories for each Book)

**Example:** Loading 46 books would trigger:
- 1 query: `SELECT * FROM books` 
- 46 queries: `SELECT * FROM book_authors WHERE book_id = ?`
- 46 queries: `SELECT * FROM book_categories WHERE book_id = ?`
- **Total: 93 queries!**

## Solution: EntityGraph with Eager Loading

We implemented `@EntityGraph` annotation to tell JPA to eagerly load related entities in a single query using LEFT JOIN FETCH.

### Changes Made to BooksRepository.java

#### 1. Added EntityGraph Import
```java
import org.springframework.data.jpa.repository.EntityGraph;
```

#### 2. Override findAll() with EntityGraph
```java
@Override
@EntityGraph(attributePaths = {"authors", "categories"})
List<Books> findAll();
```

#### 3. Override findAll(Pageable) with EntityGraph
```java
@EntityGraph(attributePaths = {"authors", "categories"})
Page<Books> findAll(Pageable pageable);
```

#### 4. Enhanced findById() with EntityGraph
```java
@Override
@EntityGraph(attributePaths = {"authors", "categories"})
@Query("SELECT b FROM Books b LEFT JOIN FETCH b.authors LEFT JOIN FETCH b.categories WHERE b.id = :id")
Optional<Books> findById(@Param("id") Integer id);
```

#### 5. Enhanced findWithFiltersAndPagination() with EntityGraph
```java
@EntityGraph(attributePaths = {"authors", "categories"})
@Query(value = "SELECT DISTINCT b FROM Books b " +
       "LEFT JOIN FETCH b.authors a " +
       "LEFT JOIN FETCH b.categories c " +
       "WHERE ...")
Page<Books> findWithFiltersAndPagination(...);
```

#### 6. Enhanced findNewestBooks() with EntityGraph
```java
@EntityGraph(attributePaths = {"authors", "categories"})
@Query("SELECT b FROM Books b LEFT JOIN FETCH b.authors LEFT JOIN FETCH b.categories ORDER BY b.id DESC")
List<Books> findNewestBooks(Pageable pageable);
```

## How @EntityGraph Works

### What It Does:
- **Attribute Paths**: `{"authors", "categories"}` tells JPA to eagerly load these related collections
- **Lazy vs Eager**: Without @EntityGraph, accessing `book.getAuthors()` would trigger another query
- **JOIN FETCH**: Combines the entities into a single result set

### SQL Generated (Before):
```sql
SELECT * FROM books;                              -- 1 query
SELECT * FROM book_authors WHERE book_id = 1;    -- N queries
SELECT * FROM book_categories WHERE book_id = 1; -- N queries
```

### SQL Generated (After):
```sql
SELECT DISTINCT b.*, a.*, c.* 
FROM books b
LEFT JOIN book_authors ba ON b.id = ba.book_id
LEFT JOIN authors a ON ba.author_id = a.id
LEFT JOIN book_categories bc ON b.id = bc.category_id
LEFT JOIN categories c ON bc.category_id = c.id;
-- 1 query with all data included!
```

## Benefits

1. **Dramatically Reduces Database Load**
   - From 93 queries → 1 query (46 books example)
   - Reduces network round trips

2. **Improves Response Time**
   - Single database hit instead of N+1 round trips
   - Especially beneficial for high-traffic endpoints (public book lists, chatbot RAG retrieval)

3. **Scales Better**
   - Performance consistent regardless of number of child entities
   - No exponential query growth

## Where This Helps

### Chatbot RAG Service
The `RagService.retrieveContext()` method calls `booksRepository.findAll()` and filters books. This now runs efficiently with @EntityGraph.

### Public Book Listing
When users browse books on the homepage, each book's authors and categories are loaded in a single query.

### Admin Searches
The `findWithFiltersAndPagination()` method now eagerly loads authors/categories for search results.

## Trade-offs to Consider

### When @EntityGraph is Good:
✅ Small to medium result sets (< 1000 records)
✅ Need to access related entities immediately
✅ Want predictable query behavior

### When @EntityGraph Could Be Problematic:
⚠️ **Cartesian Product**: If a Book has 5 authors and 5 categories, the result set multiplies (5×5=25 rows per book)
   - Solution: Use separate queries for large result sets or pagination

⚠️ **Too Many Joins**: Joining 10+ tables can be slower than multiple queries
   - Solution: Limit @EntityGraph to critical relationships only

## Current Configuration

We're eagerly loading:
- `authors` (typically 1-3 per book)
- `categories` (typically 1-3 per book)

This creates minimal cartesian product and provides good performance.

## Future Optimizations

1. **Pagination**: Always paginate to limit result set size
2. **Selective Loading**: Use different @EntityGraph strategies for different endpoints
3. **Caching**: Cache frequently accessed book lists with Spring Cache
4. **Vector Search**: Replace keyword matching with semantic embeddings (AI-powered search)

## Testing

To verify the fix works:

```bash
# Check backend logs for SQL queries
# Before: You'd see N+1 pattern
# After: You should see single JOIN query

# Monitor:
# - Books.findAll() → 1 query
# - Books.findById(id) → 1 query
# - BooksService.getAllBooks() → uses findAll() → 1 query
# - RagService.retrieveContext() → uses findAll() → 1 query
```

Enable SQL logging in `application.properties`:
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```
