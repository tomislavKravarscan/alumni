package hr.alumni.model.form;

import java.util.UUID;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PostForm {

    private UUID postId;

    @NotBlank(message = "{post.title.blank}")
    @SafeHtml(whitelistType = WhiteListType.NONE)
    private String title;

    @NotBlank(message = "{post.short-description.blank}")
    @SafeHtml(whitelistType = WhiteListType.RELAXED)
    private String shortDescription;

    @NotEmpty(message = "{post.type.blank}")
    private String[] postCategories;
    
    private String longDescription;

    @SafeHtml(whitelistType = WhiteListType.NONE)
    private String address;

    private MultipartFile picture;

    public PostForm() {
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getPostCategories() {
        return postCategories;
    }

    public void setPostCategories(String[] postCategories) {
        this.postCategories = postCategories;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

}
