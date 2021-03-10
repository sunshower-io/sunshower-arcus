package test.entities.many2many;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Cacheable
@Table(name = "BLOG_ENTRY")
public class BlogEntry {

  @Id private UUID id;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(
    name = "BLOGS_TO_TAGS",
    joinColumns = {@JoinColumn(name = "entry_id", referencedColumnName = "id")},
    inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
  )
  private Set<Tag> tags;

  public BlogEntry() {
    this.id = UUID.randomUUID();
  }

  public void addTag(Tag tag) {
    if (tags == null) {
      tags = new HashSet<>();
    }

    tag.addBlog(this);
    tags.add(tag);
  }
}
