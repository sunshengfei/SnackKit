//package com.freddon.android.snackkit.extension.realm;
//
//import com.freddon.android.snackkit.snackblogs.BuildConfig;
//import com.freddon.android.snackkit.snackblogs.model.Message;
//
//import io.realm.DynamicRealm;
//import io.realm.RealmMigration;
//import io.realm.RealmSchema;
//
///**
// * Created by fred on 2017/2/1.
// */
//public class DBRealmMigration implements RealmMigration {
//
//
//    public final static long VERSION = BuildConfig.DB_VERSION;
//
//    private DBRealmMigration(){
//
//    }
//
//    private static DBRealmMigration dBRealmMigration;
//
//    public static DBRealmMigration getInstance(){
//        if (dBRealmMigration==null){
//            dBRealmMigration=new DBRealmMigration();
//        }
//        return dBRealmMigration;
//    }
//
//    @Override
//    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
//        RealmSchema schema = realm.getSchema();
//        if (oldVersion == 0) {
//            // Migrate from v0 to v1
////            schema.create(Message.class.getName());
//            oldVersion++;
//        }
//
//        if (oldVersion == 1) {
//            // Migrate from v1 to v2
//            oldVersion++;
//        }
//
//        if (oldVersion < newVersion) {
//            throw new IllegalStateException(String.format("Migration missing from v%d to v%d", oldVersion, newVersion));
//        }
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        return super.equals(o);
//    }
//
//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }
//}
