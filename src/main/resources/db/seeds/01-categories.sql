BEGIN;

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'gasoline_1','Gasoline_1','#fffff2','#00aa01','income'::public."transaction_type",'2025-10-25 21:17:44.114'::timestamptz,'2025-10-25 22:07:02.540'::timestamptz,'','person'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'gasoline_1');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'food','Food','#f97316','bg-orange-500','expense'::public."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'🍽️','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'food');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'rent','Rent','#a855f7','bg-purple-500','expense'::public."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'🏠','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'rent');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'transport','Transport','#06b6d4','bg-cyan-500','expense'::public."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'🚗','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'transport');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'entertainment','Entertainment','#84cc16','bg-lime-500','expense'::public."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'🎬','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'entertainment');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'utilities','Utilities','#f59e0b','bg-amber-500','expense'::public."transaction_type",'2025-10-25 23:50:58.112'::timestamptz,'2025-10-25 23:50:58.112'::timestamptz,'⚡','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'utilities');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'salary','Salary','#22c55e','bg-green-500','income'::public."transaction_type",'2025-10-25 23:51:11.821'::timestamptz,'2025-10-25 23:51:11.821'::timestamptz,'💼','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'salary');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'freelance','Freelance','#3b82f6','bg-blue-500','income'::public."transaction_type",'2025-10-25 23:51:11.821'::timestamptz,'2025-10-25 23:51:11.821'::timestamptz,'💻','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'freelance');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'investment','Investment','#a855f7','bg-purple-500','income'::public."transaction_type",'2025-10-25 23:51:11.821'::timestamptz,'2025-10-25 23:51:11.821'::timestamptz,'📈','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'investment');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'gift','Gift','#ec4899','bg-pink-500','income'::public."transaction_type",'2025-10-25 23:51:11.821'::timestamptz,'2025-10-25 23:51:11.821'::timestamptz,'🎁','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'gift');

-- NECESIDADES BÁSICAS
INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'groceries','Groceries','#fb923c','bg-orange-400','expense'::public."transaction_type",NOW(),NOW(),'🛒','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'groceries');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'house_maintenance','House Maintenance','#4b5563','bg-gray-700','expense'::public."transaction_type",NOW(),NOW(),'🧹','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'house_maintenance');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'phone_internet','Phone & Internet','#0ea5e9','bg-sky-500','expense'::public."transaction_type",NOW(),NOW(),'📱','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'phone_internet');

-- SALUD Y BIENESTAR
INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'health','Health','#ef4444','bg-red-500','expense'::public."transaction_type",NOW(),NOW(),'🩺','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'health');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'mental_health','Mental Health','#6366f1','bg-indigo-500','expense'::public."transaction_type",NOW(),NOW(),'🧠','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'mental_health');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'fitness','Fitness','#22c55e','bg-green-500','expense'::public."transaction_type",NOW(),NOW(),'💪','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'fitness');

-- CRECIMIENTO PERSONAL / EDUCACIÓN
INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'education','Education','#6366f1','bg-indigo-500','expense'::public."transaction_type",NOW(),NOW(),'📚','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'education');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'courses','Courses & Workshops','#0ea5e9','bg-sky-500','expense'::public."transaction_type",NOW(),NOW(),'🎓','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'courses');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'books','Books','#15803d','bg-green-700','expense'::public."transaction_type",NOW(),NOW(),'📖','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'books');

-- RELACIONES / VIDA SOCIAL
INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'eating_out','Eating Out','#f97316','bg-orange-500','expense'::public."transaction_type",NOW(),NOW(),'🍔','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'eating_out');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'dating','Dating','#ec4899','bg-pink-500','expense'::public."transaction_type",NOW(),NOW(),'💞','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'dating');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'family','Family & Kids','#fbbf24','bg-amber-400','expense'::public."transaction_type",NOW(),NOW(),'👨‍👩‍👧','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'family');

-- OCIO / PLACER
INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'hobbies','Hobbies','#14b8a6','bg-teal-500','expense'::public."transaction_type",NOW(),NOW(),'🎨','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'hobbies');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'games','Games & Apps','#6366f1','bg-indigo-500','expense'::public."transaction_type",NOW(),NOW(),'🎮','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'games');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'travel','Travel','#0ea5e9','bg-sky-500','expense'::public."transaction_type",NOW(),NOW(),'✈️','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'travel');

-- DEUDAS / AHORRO / INVERSIÓN PERSONAL
INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'debt_payment','Debt Payment','#b91c1c','bg-red-700','expense'::public."transaction_type",NOW(),NOW(),'💳','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'debt_payment');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'savings','Savings','#22c55e','bg-green-500','expense'::public."transaction_type",NOW(),NOW(),'💰','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'savings');

-- INGRESOS MÁS DETALLADOS
INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'bonus','Bonus','#22c55e','bg-green-500','income'::public."transaction_type",NOW(),NOW(),'🏆','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'bonus');

INSERT INTO public.categories ("key","name",color_fill,color_bg,"transaction_type",created_at,updated_at,icon,"type")
SELECT 'refunds','Refunds','#a855f7','bg-purple-500','income'::public."transaction_type",NOW(),NOW(),'↩️','emoji'
    WHERE NOT EXISTS (SELECT 1 FROM public.categories WHERE "key" = 'refunds');

COMMIT;