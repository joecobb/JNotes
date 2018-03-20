package apps.joe.com.jnotes;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
    RealmConfiguration config = new RealmConfiguration.Builder()
            .name("sample.realm")
            .schemaVersion(0) // Must be bumped when the schema changes
            .migration(new MyMigration()) // Migration to run instead of throwing an exception
            .build();
    Realm.setDefaultConfiguration(config);
  }
}