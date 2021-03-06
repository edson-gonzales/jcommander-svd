/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basics operation to perform with one file or directory selected.
 * - Copy one file/directory (implemented)
 * - Delete one file/directory (not yet)
 * - Move one file/directory (not yet)
 * 
 * @version 1.0
 * @since   2016-05-19 
 * @author vania huayta
 */
public class BasicOperation {
    
    /**
     * Method to copy a file or directory with files 
     * and other directories.
     * @param source Name + source path for the file or directory to be copied
     * @param target Name + target path for the file or directory
     * @return true when the Item was copied successfully.
     */
    public static boolean copyItem(File source, File target) {
        boolean result;
        if (source.isFile()) {
            result = copyFile(source, target);
        } else {
            result = copyDirectory(source, target);
        }
        return result;
    }
    
    /**
     * Method to copy recursively a file.
     * @param source Name + source path for the file to be copied
     * @param target Name + target path for the file 
     * @return True when the file was copied successfully
     */
    private static boolean copyFile(File source, File target) {
        boolean result = false;
        try {
            //TODO REPLACE_EXISTING should be editable
            Files.copy(source.toPath(), target.toPath(), REPLACE_EXISTING);
            result = true;
        } catch (IOException ex) {
            Logger.getLogger(BasicOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    /**
     * Method to copy recursively a directory with 
     * files and other directories. 
     * @param source Name + source path for the file to be copied
     * @param target Name + target path for the file
     * @return true when the directory was copied successfully
      */
    private static boolean copyDirectory(File source, File target) {
        boolean result = false;
        if (!target.exists()) {
            target.mkdirs();
        }
        if (source.exists()) {
            for (String file : source.list()) {
                File sourceFile = new File(source, file);
                File targetFile = new File(target, file);
                copyItem(sourceFile, targetFile);
            }
        }
        
        if (Arrays.equals(source.list(), target.list()) && target.exists()) {
            result = true;
        }
        return result;
    }
    
    /**
     * Method to delete an Item
     * @param item Name + source path for the file to be deleted
     * @return true, when the Item was deleted successfully
      */
    public static boolean deleteItem(File item) {
        boolean result = false;
        if (item.isFile()) {
            try {
                result = Files.deleteIfExists(item.toPath());
            } catch (NoSuchFileException ex) {
                Logger.getLogger(BasicOperation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                // File permission problems are caught here.
                Logger.getLogger(BasicOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            result = deleteDirectory(item);
        }
        return result;
    }
    
    /**
     * Method to delete a directory
     * @param directory Name + source path for the file to be deleted
     * @return true, when the directory was deleted successfully
      */
    public static boolean deleteDirectory(File directory) {
        boolean result;
        if (directory.list().length != 0) {
            for (String file : directory.list()) {
                deleteItem(new File(directory.getAbsolutePath(), file));
            }
        }
        result = directory.delete();
        return result;
    }
    
    /**
     * Method to move an Item to a new path 
     * @param source Name + source path for the file to be moved
     * @param target Name + target path for the file
     * @return true, when an Item was moved to a new path successfully
      */
    public static boolean moveItem(File source, File target) {
        boolean result = false;
        if (copyItem(source, target)) {
            deleteItem(source);
            result = true;
        }
        return result;
    }
    
    /**
     * Method to create a single file with extension in a path specified
     * @param name String that contains the name of the file to be created
     * @param path File with the path where the new file will be created.
     * @return true if the file was created without errors, false if it was not possible to create the file.
     */
    public static boolean createFile(String name, File path){
        boolean result = false;
        File newFile = new File (path.getAbsolutePath(), name);
        try {
            result = newFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(BasicOperation.class.getName()).log(Level.SEVERE, "It was not possible to create the file", ex);
        }
        return result;
    }
    
    /**
     * Method to create a single directory in a path specified.
     * @param name String that contains the name of the directory to be created.
     * @param path File with the path where the new directory will be created.
     * @return true if the directory was created without errors, false if the directory already exists.
     */
    public static boolean createDirectory(String name, File path){
        File newDirectory = new File (path.getAbsolutePath(), name);
        boolean result = false;
        if (!newDirectory.exists())
            result = newDirectory.mkdir();
        
        return result;
    }
    
    /**
     * Method to rename an item, it works for single file or directory.
     * Old file is deleted from the system.
     * @param oldItem File that contains the path and old name of the file.
     * @param newItem File that contains the path and the new name of the file.
     * @return true if the file with the new name exists.
     */
    public static boolean renameItem(File oldItem, File newItem) {
        boolean result = false;
        if (!newItem.exists())
            result = oldItem.renameTo(newItem);
        
        return (result && (!oldItem.exists()));
    }
}