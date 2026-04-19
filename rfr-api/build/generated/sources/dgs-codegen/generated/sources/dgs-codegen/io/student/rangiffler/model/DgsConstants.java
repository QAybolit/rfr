package io.student.rangiffler.model;

import java.lang.String;

public class DgsConstants {
  public static final String QUERY_TYPE = "Query";

  public static final String MUTATION_TYPE = "Mutation";

  public static class QUERY {
    public static final String TYPE_NAME = "Query";

    public static final String Countries = "countries";

    public static final String User = "user";

    public static final String Users = "users";

    public static final String Feed = "feed";

    public static class USERS_INPUT_ARGUMENT {
      public static final String Page = "page";

      public static final String Size = "size";

      public static final String SearchQuery = "searchQuery";
    }

    public static class FEED_INPUT_ARGUMENT {
      public static final String WithFriends = "withFriends";
    }
  }

  public static class MUTATION {
    public static final String TYPE_NAME = "Mutation";

    public static final String User = "user";

    public static final String Photo = "photo";

    public static final String DeletePhoto = "deletePhoto";

    public static final String Friendship = "friendship";

    public static class USER_INPUT_ARGUMENT {
      public static final String Input = "input";
    }

    public static class PHOTO_INPUT_ARGUMENT {
      public static final String Input = "input";
    }

    public static class DELETEPHOTO_INPUT_ARGUMENT {
      public static final String Id = "id";
    }

    public static class FRIENDSHIP_INPUT_ARGUMENT {
      public static final String Input = "input";
    }
  }

  public static class USER {
    public static final String TYPE_NAME = "User";

    public static final String Id = "id";

    public static final String Username = "username";

    public static final String Firstname = "firstname";

    public static final String Surname = "surname";

    public static final String Avatar = "avatar";

    public static final String FriendStatus = "friendStatus";

    public static final String Friends = "friends";

    public static final String IncomeInvitations = "incomeInvitations";

    public static final String OutcomeInvitations = "outcomeInvitations";

    public static final String Location = "location";

    public static class FRIENDS_INPUT_ARGUMENT {
      public static final String Page = "page";

      public static final String Size = "size";

      public static final String SearchQuery = "searchQuery";
    }

    public static class INCOMEINVITATIONS_INPUT_ARGUMENT {
      public static final String Page = "page";

      public static final String Size = "size";

      public static final String SearchQuery = "searchQuery";
    }

    public static class OUTCOMEINVITATIONS_INPUT_ARGUMENT {
      public static final String Page = "page";

      public static final String Size = "size";

      public static final String SearchQuery = "searchQuery";
    }
  }

  public static class FEED {
    public static final String TYPE_NAME = "Feed";

    public static final String Username = "username";

    public static final String WithFriends = "withFriends";

    public static final String Photos = "photos";

    public static final String Stat = "stat";

    public static class PHOTOS_INPUT_ARGUMENT {
      public static final String Page = "page";

      public static final String Size = "size";
    }
  }

  public static class STAT {
    public static final String TYPE_NAME = "Stat";

    public static final String Count = "count";

    public static final String Country = "country";
  }

  public static class PHOTO {
    public static final String TYPE_NAME = "Photo";

    public static final String Id = "id";

    public static final String Src = "src";

    public static final String Country = "country";

    public static final String Description = "description";

    public static final String CreationDate = "creationDate";

    public static final String Likes = "likes";

    public static final String IsOwner = "isOwner";
  }

  public static class LIKES {
    public static final String TYPE_NAME = "Likes";

    public static final String Total = "total";

    public static final String Likes = "likes";
  }

  public static class LIKE {
    public static final String TYPE_NAME = "Like";

    public static final String User = "user";

    public static final String Username = "username";

    public static final String CreationDate = "creationDate";
  }

  public static class COUNTRY {
    public static final String TYPE_NAME = "Country";

    public static final String Flag = "flag";

    public static final String Code = "code";

    public static final String Name = "name";
  }

  public static class FRIENDSHIPINPUT {
    public static final String TYPE_NAME = "FriendshipInput";

    public static final String User = "user";

    public static final String Action = "action";
  }

  public static class PHOTOINPUT {
    public static final String TYPE_NAME = "PhotoInput";

    public static final String Id = "id";

    public static final String Src = "src";

    public static final String Country = "country";

    public static final String Description = "description";

    public static final String Like = "like";
  }

  public static class LIKEINPUT {
    public static final String TYPE_NAME = "LikeInput";

    public static final String User = "user";
  }

  public static class USERINPUT {
    public static final String TYPE_NAME = "UserInput";

    public static final String Firstname = "firstname";

    public static final String Surname = "surname";

    public static final String Avatar = "avatar";

    public static final String Location = "location";
  }

  public static class COUNTRYINPUT {
    public static final String TYPE_NAME = "CountryInput";

    public static final String Code = "code";
  }
}
