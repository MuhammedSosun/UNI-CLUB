package com.uniClub.user.api;

import java.util.UUID;

public interface UserPublicService {

    UUID getUserIdByUsername(String username);
}
