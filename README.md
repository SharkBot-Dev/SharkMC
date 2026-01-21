# SharkMC
Minecraftの多機能プラグイン

# 対応バージョン
1.21.11だと思われる

# できること
・Webダッシュボードからの管理<br>
・メンバーのモデレート<br>
・AFK

# セットアップ方法
/pluginsに、tagsから入手した、最新の.jarファイルを導入する<br>

Windowsの場合<br>
run.ps1を作成し、以下のように書き込む
```
$Env:USERNAME = "ユーザー名"
$Env:PASSWORD = "パスワード"
java -jar サーバーのメインファイル.jar
```

Linuxの場合<br>
run.shを作成し、以下のように書き込む
```
export USERNAME="ユーザー名"
export PASSWORD="パスワード"
java -jar サーバーのメインファイル.jar
```

# 実行方法
Windowsの場合<br>
```
.\run.ps1
```

Linuxの場合<br>
```
bash run.sh
```
