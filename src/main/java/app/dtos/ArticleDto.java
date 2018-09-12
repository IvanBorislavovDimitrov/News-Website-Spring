package app.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleDto {
	
	private int id;
	private String name;
	private String description;
	private String date;
	private List<CommentDto> comments;
	
	public ArticleDto() {
		this.comments = new ArrayList<>();
	}
	
	public ArticleDto(int id, String name, String description, String date) {
		this();
		this.id = id;
		this.name = name;
		this.description = description;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<CommentDto> getComments() {
		return comments;
	}

	public void setComments(List<CommentDto> comments) {
		this.comments = comments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
}
