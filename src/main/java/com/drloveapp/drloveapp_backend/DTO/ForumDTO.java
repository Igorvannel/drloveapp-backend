package com.drloveapp.drloveapp_backend.DTO;

import com.drloveapp.drloveapp_backend.model.ForumCategory;
import com.drloveapp.drloveapp_backend.model.ForumComment;
import com.drloveapp.drloveapp_backend.model.ForumPost;
import com.drloveapp.drloveapp_backend.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ForumDTO {

    // DTO pour un post de forum avec infos de base
    public static class PostSummaryDTO {
        private Long id;
        private String title;
        private String content;
        private String categoryName;
        private Long categoryId;
        private Long authorId;
        private String authorName;
        private String authorAvatar;
        private String imageUrl;
        private int likes;
        private int commentsCount;
        private int views;
        private int shares;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static PostSummaryDTO fromEntity(ForumPost post) {
            PostSummaryDTO dto = new PostSummaryDTO();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());

            // Limiter le contenu à 200 caractères pour la vue résumée
            String content = post.getContent();
            if (content.length() > 200) {
                content = content.substring(0, 197) + "...";
            }
            dto.setContent(content);

            dto.setCategoryName(post.getCategory().getName());
            dto.setCategoryId(post.getCategory().getId());

            User author = post.getAuthor();
            dto.setAuthorId(author.getId());
            dto.setAuthorName(author.getFullName());


            dto.setImageUrl(post.getImageUrl());
            dto.setLikes(post.getLikes());
            dto.setCommentsCount(post.getCommentsCount());
            dto.setViews(post.getViews());
            dto.setShares(post.getShares());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setUpdatedAt(post.getUpdatedAt());

            return dto;
        }

        // Getters et Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public Long getAuthorId() {
            return authorId;
        }

        public void setAuthorId(Long authorId) {
            this.authorId = authorId;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorAvatar() {
            return authorAvatar;
        }

        public void setAuthorAvatar(String authorAvatar) {
            this.authorAvatar = authorAvatar;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(int commentsCount) {
            this.commentsCount = commentsCount;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public int getShares() {
            return shares;
        }

        public void setShares(int shares) {
            this.shares = shares;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    // DTO pour un post de forum avec détails complets
    public static class PostDetailDTO extends PostSummaryDTO {
        private List<CommentDTO> comments;

        public static PostDetailDTO fromEntity(ForumPost post) {
            PostDetailDTO dto = new PostDetailDTO();

            // Copier tous les champs de base
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent()); // Contenu complet pour la vue détaillée
            dto.setCategoryName(post.getCategory().getName());
            dto.setCategoryId(post.getCategory().getId());

            User author = post.getAuthor();
            dto.setAuthorId(author.getId());
            dto.setAuthorName(author.getFullName());


            dto.setImageUrl(post.getImageUrl());
            dto.setLikes(post.getLikes());
            dto.setCommentsCount(post.getCommentsCount());
            dto.setViews(post.getViews());
            dto.setShares(post.getShares());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setUpdatedAt(post.getUpdatedAt());

            // Convertir les commentaires
            dto.setComments(post.getComments().stream()
                    .map(CommentDTO::fromEntity)
                    .collect(Collectors.toList()));

            return dto;
        }

        public List<CommentDTO> getComments() {
            return comments;
        }

        public void setComments(List<CommentDTO> comments) {
            this.comments = comments;
        }
    }

    // DTO pour les commentaires
    public static class CommentDTO {
        private Long id;
        private String text;
        private Long authorId;
        private String authorName;
        private String authorAvatar;
        private Long postId;
        private int likes;
        private LocalDateTime createdAt;

        public static CommentDTO fromEntity(ForumComment comment) {
            CommentDTO dto = new CommentDTO();
            dto.setId(comment.getId());
            dto.setText(comment.getText());

            User author = comment.getAuthor();
            dto.setAuthorId(author.getId());
            dto.setAuthorName(author.getFullName());


            dto.setPostId(comment.getPost().getId());
            dto.setLikes(comment.getLikes());
            dto.setCreatedAt(comment.getCreatedAt());

            return dto;
        }

        // Getters et Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Long getAuthorId() {
            return authorId;
        }

        public void setAuthorId(Long authorId) {
            this.authorId = authorId;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getAuthorAvatar() {
            return authorAvatar;
        }

        public void setAuthorAvatar(String authorAvatar) {
            this.authorAvatar = authorAvatar;
        }

        public Long getPostId() {
            return postId;
        }

        public void setPostId(Long postId) {
            this.postId = postId;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }

    // DTO pour les catégories
    // DTO pour les catégories
    public static class CategoryDTO {
        private Long id;
        private String name;
        private int displayOrder;
        private int postCount;

        public static CategoryDTO fromEntity(ForumCategory category) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setDisplayOrder(category.getDisplayOrder());
            dto.setPostCount(category.getPosts().size());

            return dto;
        }

        // Getters et Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
        }

        public int getPostCount() {
            return postCount;
        }

        public void setPostCount(int postCount) {
            this.postCount = postCount;
        }
    }

    // DTO pour la pagination des posts
    public static class PostPageResponseDTO {
        private List<PostSummaryDTO> posts;
        private int currentPage;
        private int totalPages;
        private long totalPosts;

        public List<PostSummaryDTO> getPosts() {
            return posts;
        }

        public void setPosts(List<PostSummaryDTO> posts) {
            this.posts = posts;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public long getTotalPosts() {
            return totalPosts;
        }

        public void setTotalPosts(long totalPosts) {
            this.totalPosts = totalPosts;
        }
    }
}