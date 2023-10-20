package br.com.todoist.todoist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity(name = "tb_tasks")
public class TaskModel {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;
  private String description;

  @Column(length = 70)
  private String title;
  private LocalDateTime endAt;
  private String priority;
  private String taskGroup;

  private UUID idUser;

  @CreationTimestamp
  private LocalDateTime createdAt;


  public void setTitle(String title) throws Exception {
    if(title.length() > 50) {
      throw new Exception("Title must be up to 70 characters");
    }
    this.title = title;
  }
}