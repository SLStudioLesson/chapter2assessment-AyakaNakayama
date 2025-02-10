package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

import data.RecipeFileHandler;

public class RecipeUI {
    private BufferedReader reader;
    private RecipeFileHandler fileHandler;

    public RecipeUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileHandler = new RecipeFileHandler();
    }

    public RecipeUI(BufferedReader reader, RecipeFileHandler fileHandler) {
        this.reader = reader;
        this.fileHandler = fileHandler;
    }

    public void displayMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        // 設問1: 一覧表示機能
                        this.displayRecipes();
                        break;
                    case "2":
                        // 設問2: 新規登録機能
                        this.addNewRecipe();
                        break;
                    case "3":
                        // 設問3: 検索機能
                        this.searchRecipe();
                        break;
                    case "4":
                        System.out.println("Exit the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    /**
     * 設問1: 一覧表示機能
     * RecipeFileHandlerから読み込んだレシピデータを整形してコンソールに表示します。
     */
    private void displayRecipes() {
        // レシピを取得するメソッドを実行
        ArrayList<String> recipes = fileHandler.readRecipes();
        if (recipes == null) {
            System.out.println("No recipes available.");
            return;
        }
        // 取得したArrayListからレシピを一行ずつ出力
        System.out.println("Recipes:");
        for (String recipe : recipes){
            String[] names = recipe.split(" ", 2);
            System.out.println("-----------------------------------");
            System.out.println("Recipe Name: " + names[0]);
            System.out.println("Main Ingredients: " + names[1]);
        }
        System.out.println("-----------------------------------");
    }

    /**
     * 設問2: 新規登録機能
     * ユーザーからレシピ名と主な材料を入力させ、RecipeFileHandlerを使用してrecipes.txtに新しいレシピを追加します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void addNewRecipe() throws IOException {
        // レシピ名入力受付
        System.out.print("Enter recipe name: ");
        String recipeName = reader.readLine();

        // 材料入力受付
        System.out.print("Enter main ingredients (comma separated): ");
        String ingredients = reader.readLine();

        // レシピ書き込み処理呼び出し
        fileHandler.addRecipe(recipeName, ingredients);
        System.out.println("Recipe added successfully.");
    }

    /**
     * 設問3: 検索機能
     * ユーザーから検索クエリを入力させ、そのクエリに基づいてレシピを検索し、一致するレシピをコンソールに表示します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void searchRecipe() throws IOException {
        // レシピの一覧を取得
        ArrayList<String> recipes = fileHandler.readRecipes();

        // 検索するクエリを入力受付
        System.out.print("Enter search query (e.g., 'name=Tomato&ingredient=Garlic'): ");
        String query = reader.readLine();

        // 文字列を検索
        // 条件ごとに分割
        String[] searchItems = query.split("&");
        for (String searchItem : searchItems){
            // 検索対象の名前を取得
            String[] pairs = searchItem.split("=");

            // レシピの一覧から一行ずつ等しい文字列があるか確認する
            int searchedCount = 0;
            for (String recipe : recipes){
                int searchNum = searchItems.length;
                String[] recipeItems = recipe.split(" ", 2);

                // 変数name(レシピ名)の検索
                if ("name".equals(pairs[0])){
                    // System.out.println(pairs[1].compareTo(recipeItems[0]));
                    if (pairs[1].compareTo(recipeItems[0])==0) searchNum -= 1;
                    // System.out.println(recipeItems[0]);
                }
                // 変数ingredient(主な材料)の検索
                else if ("ingredient".equals(pairs[0])){
                    // System.out.println("ingredient");
                    int searchStart = recipeItems[1].indexOf(pairs[1].toCharArray()[0]);
                    System.out.println(searchStart);
                    // if (pairs[1].compareTo(recipeItems[1].substring(searchStart, searchStart + pairs[1].length()))) searchNum -= 1;

                }

                // 検索して一致していた個数が検索する条件数に一致したら出力
                if (searchNum == 0) {
                    System.out.println(recipe);
                    searchedCount++;
                }
            }

            if (searchedCount == 0) System.out.println("No recipes found matching the criteria.");
        }
    }

}

