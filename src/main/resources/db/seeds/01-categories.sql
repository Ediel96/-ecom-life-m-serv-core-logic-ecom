BEGIN;

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'gasoline_1','Gasoline_1','#fffff2','#00aa01','income'::public2."transaction_type",'2025-10-25 21:17:44.114'::timestamptz,'2025-10-25 22:07:02.540'::timestamptz,'','person'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'gasoline_1');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'food','Food','#f97316','bg-orange-500','expense'::public2."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'🍽️','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'food');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'rent','Rent','#a855f7','bg-purple-500','expense'::public2."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'🏠','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'rent');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'transport','Transport','#06b6d4','bg-cyan-500','expense'::public2."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'🚗','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'transport');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'entertainment','Entertainment','#84cc16','bg-lime-500','expense'::public2."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'🎬','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'entertainment');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'utilities','Utilities','#f59e0b','bg-amber-500','expense'::public2."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'⚡','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'utilities');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'salary','Salary','#22c55e','bg-green-500','income'::public2."transaction_type",'2025-10-25 23:51:11.821'::timestamptz,'2025-10-25 23:51:11.821'::timestamptz,'💼','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'salary');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'freelance','Freelance','#3b82f6','bg-blue-500','income'::public2."transaction_type",'2025-10-25 23:51:11.821'::timestamptz,'2025-10-25 23:51:11.821'::timestamptz,'💻','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'freelance');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'investment','Investment','#a855f7','bg-purple-500','income'::public2."transaction_type",'2025-10-25 23:51:11.821'::timestamptz,'2025-10-25 23:51:11.821'::timestamptz,'📈','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'investment');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'gift','Gift','#ec4899','bg-pink-500','income'::public2."transaction_type",'2025-10-25 23:51:11.821'::timestamptz,'2025-10-25 23:51:11.821'::timestamptz,'🎁','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public2.categories WHERE "key" = 'gift');

COMMIT;