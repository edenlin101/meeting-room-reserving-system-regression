package app.resource.service;

import app.resource.api.room.CreateRoomRequest;
import app.resource.api.room.CreateRoomResponse;
import app.resource.api.room.ListRoomRequest;
import app.resource.api.room.ListRoomResponse;
import app.resource.api.room.RoomView;
import app.resource.domain.Room;
import core.framework.db.Repository;
import core.framework.inject.Inject;
import core.framework.web.exception.ConflictException;

import java.util.List;
import java.util.stream.Collectors;

public class RoomService {
    @Inject
    Repository<Room> roomRepository;

    public CreateRoomResponse create(CreateRoomRequest request) {
        if (!roomRepository.select("name = ? AND company_id = ?", request.name, request.companyId).isEmpty()) {
            throw new ConflictException("room already exists in the company");
        }

        Room room = new Room();
        room.name = request.name;
        room.companyId = request.companyId;
        room.id = roomRepository.insert(room).orElseThrow();

        CreateRoomResponse response = new CreateRoomResponse();
        response.id = room.id;
        response.name = room.name;
        response.companyId = room.companyId;
        return response;
    }

    public void delete(Long id) {
        roomRepository.delete(id);
    }

    public ListRoomResponse list(ListRoomRequest request) {
        List<Room> rooms;
        if (request != null && request.companyId != null) {
            rooms = roomRepository.select("company_id = ?", request.companyId);
        } else {
            rooms = roomRepository.select("1=1");
        }
        
        ListRoomResponse response = new ListRoomResponse();
        response.rooms = rooms.stream().map(r -> {
            RoomView view = new RoomView();
            view.id = r.id;
            view.name = r.name;
            view.companyId = r.companyId;
            return view;
        }).collect(Collectors.toList());
        return response;
    }
}