package dev.gabrielayres.Todolist.users;

import java.util.List;

public record RegisterDTO(String username, String name, String password, String telephone, List<String> groups , UserRoles role) {
}
