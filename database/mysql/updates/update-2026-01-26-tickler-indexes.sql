-- Performance indexes for tickler queries
-- These indexes support common filter operations and batch fetching

-- Critical: Index for batch fetching tickler links (N+1 fix)
CREATE INDEX IF NOT EXISTS `idx_tickler_link_tickler_no` ON `tickler_link` (`tickler_no`);

-- Index for "assigned to" filter queries
CREATE INDEX IF NOT EXISTS `idx_tickler_task_assigned_to` ON `tickler` (`task_assigned_to`);

-- Index for "created by" filter queries
CREATE INDEX IF NOT EXISTS `idx_tickler_creator` ON `tickler` (`creator`);
