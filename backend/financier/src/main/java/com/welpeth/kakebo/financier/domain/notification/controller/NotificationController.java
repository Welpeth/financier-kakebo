package com.welpeth.kakebo.financier.domain.notification.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.notification.entity.Notification;
import com.welpeth.kakebo.financier.domain.notification.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.NOTIFICATION)
public class NotificationController {

  private final NotificationService service;

  @GetMapping("/list")
  public ResponseEntity<List<Notification>> getList() {
    return ResponseEntity.ok(service.getList());
  }

  @GetMapping("/unread-count")
  public ResponseEntity<Long> unreadCount() {
    return ResponseEntity.ok(service.unreadCount());
  }

  @PatchMapping("/{id}/read")
  public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
    service.markAsRead(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/read-all")
  public ResponseEntity<Void> markAllAsRead() {
    service.markAllAsRead();
    return ResponseEntity.noContent().build();
  }
}
