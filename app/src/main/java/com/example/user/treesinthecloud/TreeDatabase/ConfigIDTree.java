package com.example.user.treesinthecloud.TreeDatabase;

public class ConfigIDTree {
    //public static final String DATA_URL = "https://a15_ee5_trees1.studev.groept.be/sortID.php?idTrees=";
    public static final String DATA_URL = "http://projectmovie.16mb.com/sortID.php?idTrees=";
    public static final String URL_GET_TREE_AREA = "http://projectmovie.16mb.com/showTreesInArea.php";

    public static final String JSON_ARRAY = "result";

    //public static final String URL_GET_ALL = "https://a15_ee5_trees1.studev.groept.be/showAllTrees.php";
    public static final String URL_GET_ALL = "http://projectmovie.16mb.com/showAllTrees.php";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "idTrees";
    public static final String TAG_LONGITUDE = "longitude";
    public static final String TAG_LATITUDE = "latitude";
    public static final String TAG_SPECIE = "specie";
    public static final String TAG_STATUS = "status";
    public static final String TAG_COMMON_NAME = "common name";
    public static final String TAG_ORIGINAL_GIRTH = "original girth";
    public static final String TAG_CURRENT_GIRTH = "current girth";
    public static final String TAG_CUTTING_SHAPE = "cutting shape";
	
    public static final String URL_GET_LIKES = "http://projectmovie.16mb.com/FetchLikesByTreeId.php";
    public static final String URL_lIKE_TREE = "http://projectmovie.16mb.com/likeTree.php";
    public static final String URL_DISlIKE_TREE = "http://projectmovie.16mb.com/dislikeTree.php";
    public static final String URL_HASLIKED_TREE = "http://projectmovie.16mb.com/hasliked.php";
    public static final String URL_COMMENT_TREE = "http://projectmovie.16mb.com/setComment.php";
    public static final String URL_GET_ALL_COMMENTS_TREE = "http://projectmovie.16mb.com/FetchCommentsByTreeId.php";
    public static final String URL_DELETE_COMMENT_TREE = "http://projectmovie.16mb.com/DeleteComment.php";
    public static final String URL_FIND_USER = "http://projectmovie.16mb.com/FindUser.php";

}
