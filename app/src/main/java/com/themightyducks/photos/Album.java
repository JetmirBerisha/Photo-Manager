package com.themightyducks.photos;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private String name;
    private ArrayList<CustomImage> images;

    /**
     * Small wrapper for an album
     * @param name Album name.
     */
    public Album(String name) {
        this.name = name;
        images = new ArrayList<>();
    }

    /**
     * Getter
     * @return the name of the album
     */
    public String getName() {
        return name;
    }

    /**
     * Setter
     * @param name the new name for the album
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the index of this image in the album
     * @param img the image in the album
     * @return the index of the image in the album
     */
    public int indexOf(CustomImage img){
        return images.indexOf(img);
    }

    /**
     * @return The string representation of this object
     */
    public String toString() {
        return name;
    }

    /**
     * A manner to retrieve the images in this album
     * @return The images that this album contains as an ArrayList
     */
    public ArrayList<CustomImage> getImages() {
        return images;
    }

    /**
     * Add a new image to the images that this album contains
     * @param newImg The new image to be added
     */
    public void addImage(CustomImage newImg) {
        images.add(newImg);
    }

    public void remove(CustomImage img) {
        images.remove(img);
    }
}
    /*
    HOW TO IMPORT FILE FROM PATH:

    File imgFile = new  File(“filepath”);
    if(imgFile.exists()) {
        ImageView myImage = new ImageView(this);
        myImage.setImageURI(Uri.fromFile(imgFile));
    }
    */

//    private FrameLayout root;
//    private TextView name;
//    private LinearLayout options;
//    private Button edit;
//    private Button delete;
//    private AlbumView caller;
//    private long touchTime;
//
//    /**
//     * New album view in the given context with the given name.
//     * @param albumName The new album name.
//     * @param caller The context of this album
//     */
//    public Album(String albumName, AlbumView caller){
//        super(caller);
//        this.caller = caller;
//        // Load/Inflate the xml into code
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        root = (FrameLayout) inflater.inflate(R.layout.single_album, root, false);
//        name = root.findViewById(R.id.textView);
//        options = root.findViewById(R.id.options);
//        edit = root.findViewById(R.id.edit_btn);
//        delete = root.findViewById(R.id.del_btn);
//        options.setVisibility(INVISIBLE);
//        name.setText(albumName);
//
//        setEditHandler((l) -> {
//            // Start the edit screen
//            Bundle bund = new Bundle();
//            bund.putInt(AddEditAlbum.ALBUM_INDEX, this.caller.albumAdapterFeed.indexOf(this));
//            bund.putString(AddEditAlbum.ALBUM_NAME, name.getText().toString());
//            Intent intent = new Intent(this.caller, AddEditAlbum.class);
//            intent.putExtras(bund);
//            this.caller.startActivityForResult(intent, AlbumView.EDIT_CODE);
//            this.caller.topG.setAdapter(new ArrayAdapter<>(this.caller, R.layout.single_album, this.caller.albumAdapterFeed));
//        });
//
//        setDeleteHandler((l) -> {
//            this.caller.albumAdapterFeed.remove(this);
//            this.caller.topG.setAdapter(new ArrayAdapter<>(this.caller, R.layout.single_album, this.caller.albumAdapterFeed));
//        });
//        setOnTouchListener( (view, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                // Start timer
//                hideOptions();
//                touchTime = System.currentTimeMillis();
//            }
//            else if (event.getAction() == MotionEvent.ACTION_UP) {
//                if (touchTime != 0 && System.currentTimeMillis() - touchTime > 1000){
//                    touchTime = 0;
//                    showOptions();
//                }
//                else {
//                    // Open photoView
//                }
//            }
//            return true;
//        });
//    }
//
//    /**
//     * Set the handler for edit button in an album.
//     * @param l Handler.
//     */
//    private void setEditHandler(OnClickListener l) {
//        edit.setOnClickListener(l);
//    }
//
//    /**
//     * Set the handler for delete button in an album.
//     * @param l Handler.
//     */
//    private void setDeleteHandler(OnClickListener l) {
//        delete.setOnClickListener(l);
//    }
//
//    /**
//     * T.
//     * @return The album name
//     */
//    public String getAlbumName(){
//        return name.getText().toString();
//    }
//
//    /**
//     * Makes the edit and delete buttons visible.
//     */
//    public void showOptions() {
//        options.setVisibility(VISIBLE);
//    }
//
//    /**
//     * Makes the edit and delete buttons invisible.
//     */
//    public void hideOptions() {
//        options.setVisibility(INVISIBLE);
//    }
//
//    /**
//     * Set the name of this album.
//     * @param n The new name to be used
//     */
//    public void setAlbumName(String n) {
//        name.setText(n);
//    }
//
//    public FrameLayout getView() {
//        return root;
//    }

