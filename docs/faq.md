# Frequently Asked Questions & OEM Troubleshooting

---

## OEM Battery Optimization Troubleshooting

Custom Android OEM systems (Xiaomi HyperOS/MIUI, Samsung One UI, Oppo ColorOS, Vivo FuntouchOS) aggressively kill background processes and disable Accessibility Services to save battery.

If ReelLock stops working after phone restart or extended lock periods, follow these device-specific instructions:

### Xiaomi / Redmi / Poco (HyperOS & MIUI)
1. Open **Settings** -> **Apps** -> **Manage Apps** -> **ReelLock**.
2. Enable **Autostart**.
3. Under **Battery Saver**, select **No Restrictions**.
4. Lock ReelLock in the **Recent Apps** screen (press and hold ReelLock card -> tap lock icon).

### Samsung Galaxy (One UI)
1. Open **Settings** -> **Apps** -> **ReelLock**.
2. Tap **Battery** -> select **Unrestricted**.
3. Open **Settings** -> **Device Care** -> **Battery** -> **Background usage limits** -> add ReelLock to **Never sleeping apps**.

### OnePlus / Oppo / Realme (OxygenOS & ColorOS)
1. Open **Settings** -> **Battery** -> **More Settings** -> **Optimize battery use** -> set ReelLock to **Don't optimize**.
2. Enable **Allow background activity** under App Info.

---

## Frequently Asked Questions

### Does ReelLock access my personal messages or Instagram data?
**No.** ReelLock requires zero internet permissions (`android.permission.INTERNET`). It inspects view structural layouts on-device solely to identify if a Reel player view container is rendering.

### Why does Accessibility Service turn off automatically?
Android automatically disables Accessibility Services if the app is killed by aggressive battery management software. Ensure you follow the OEM battery optimization steps above to keep ReelLock active.

### Can I still watch regular posts and IGTV videos?
Yes! ReelLock is specifically trained to detect the Instagram Reels player container (`clips_viewer` / Reels tab), leaving home feed posts, stories, and direct messages completely accessible.
