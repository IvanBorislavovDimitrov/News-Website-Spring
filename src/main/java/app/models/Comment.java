package app.models;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "value", length = 500)
	private String value;
	
	@Basic
	private Date date;

	@ManyToOne
	@JoinColumn(name = "new")
	private Article article;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	public Comment() {

	}

	public Comment(String value, Article article, User user) {
		super();
		this.value = value;
		this.article = article;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
