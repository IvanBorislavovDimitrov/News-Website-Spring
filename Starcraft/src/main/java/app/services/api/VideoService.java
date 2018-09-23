package app.services.api;

import app.models.Video;

import java.util.List;

public interface VideoService {

    void save(String name);

    List<Video> getAll();
}
