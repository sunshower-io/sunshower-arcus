package test.entities.many2many;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Table(name = "TAG")
public class Tag {

  @Id private UUID id;

  @Basic private String name;

  @ManyToMany(mappedBy = "tags")
  private Set<BlogEntry> blogs;

  public Tag(String id) {
    this();
    this.name = name;
  }

  public Tag() {
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<BlogEntry> getBlogs() {
    return blogs;
  }

  public void setBlogs(Set<BlogEntry> blogs) {
    this.blogs = blogs;
  }

  public void addBlog(BlogEntry blogEntry) {
    if (this.blogs == null) {
      this.blogs = new HashSet<>();
    }
    this.blogs.add(blogEntry);
  }
}
